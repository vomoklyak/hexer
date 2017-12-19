package com.hexer.provider;

import com.hexer.exception.HexerApplicationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultMessageDigestProviderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private DefaultMessageDigestProvider sut;

    @Test
    public void whenProvideMessageDigestSHA512ThenReturnMessageDigestSHA512() {
        // Given
        final String sha512 = "SHA-512";
        sut = new DefaultMessageDigestProvider(sha512);

        // When
        final MessageDigest result = sut.provide();

        // Then
        assertThat(result, notNullValue());
        assertThat(result.getAlgorithm(), equalTo(sha512));
    }


    @Test
    public void whenProvideMessageDigestMultipleTimesFromSameThreadThenReturnSameMessageDigestInstance() {
        // Given
        final String sha512 = "SHA-512";
        sut = new DefaultMessageDigestProvider(sha512);

        // When
        final MessageDigest resultOne = sut.provide();
        final MessageDigest resultTwo = sut.provide();

        // Then
        assertThat(resultOne, sameInstance(resultTwo));
    }

    @Test
    public void whenProvideMessageDigestMultipleTimesFromDifferentThreadThenReturnDifferentMessageDigestInstance() throws InterruptedException {
        // Given
        final String sha512 = "SHA-512";
        final List<MessageDigest> result = Collections.synchronizedList(new ArrayList<>());
        sut = new DefaultMessageDigestProvider(sha512);

        // When
        final Thread threadOne = new Thread(() -> result.add(sut.provide()));
        final Thread threadTwo = new Thread(() -> result.add(sut.provide()));
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();

        // Then
        assertThat(result.get(0), not(sameInstance(result.get(1))));
    }

    @Test
    public void whenProvideMessageDigestWithIncorrectAlgorithmThenException() {
        // Given
        final String incorrectAlgorithm = "Incorrect_Algorithm";
        sut = new DefaultMessageDigestProvider(incorrectAlgorithm);

        // When
        exception.expect(HexerApplicationException.class);
        sut.provide();
    }

}