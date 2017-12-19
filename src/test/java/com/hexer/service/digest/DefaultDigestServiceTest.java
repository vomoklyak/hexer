package com.hexer.service.digest;

import com.hexer.exception.HexerApplicationException;
import com.hexer.provider.DefaultMessageDigestProvider;
import com.hexer.util.HexUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultDigestServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DefaultDigestService sut;

    @Before
    public void init() {
        sut = new DefaultDigestService(1024, new DefaultMessageDigestProvider("SHA-512"));
    }

    @Test
    public void whenDigestFileThenReturnHash() {
        // Given
        final File file = new File("src/test/resources/java/com/hexer/service/digest/fileA.dat");

        // When
        final String result = HexUtil.toHex(sut.digest(file));

        // Then
        assertThat(result, equalTo("af371785c4fecf30acdd648a7d4d649901eeb67536206a9f517768f0851c0a06616f724b2a194e7bc0a762636c55fc34e0fcaf32f1e852682b2b07a9d7b7a9f9"));
    }

    @Test
    public void whenDigestHashArraysThenReturnHash() {
        // Given
        final byte[] firstFileHash
                = "9dd88c920d86ac24112eb692e87b047bb6e69cd413593b009af62a29a71daa68f094dd3340976ae9b8e5d8e5d66d964179409c049103f91f3ccba80d9de63b7a".getBytes();
        final byte[] secondFileHash
                = "40c9964826072dbebe00ea99db34a8c8268088738de8d2a9c02743d0eed36a018adf122bacd789cc569ba2f5f54c75191683e3f252486bf71a5824ae99e20017".getBytes();
        // When
        final String result = HexUtil.toHex(sut.digest(new byte[][]{firstFileHash, secondFileHash}));

        // Then
        assertThat(result, equalTo("9f0c752149eb2699f077215798e03f16837ec85fda55a57efeee6480e8ee43971092deec7ff553476d53f0760d637d41b2c31be2b4ef55614ab5d17ab0f8f6dc"));
    }

    @Test
    public void whenDigestNonexistentFileThenException() {
        // Given
        final File NonexistentFile = new File("/nonexistent/nonexistentFile.dat");

        // When
        expectedException.expect(HexerApplicationException.class);
        sut.digest(NonexistentFile);
    }

}