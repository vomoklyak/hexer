package com.hexer;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApplicationIT {

    private final String input = "src/test/resources/java/com/hexer/service/input";
    private final String output = "src/test/resources/java/com/hexer/service/output.dat";

    @Before
    public void before() throws IOException {
        Assume.assumeTrue(System.getProperty("os.name").toLowerCase().contains("windows"));
        Files.deleteIfExists(Paths.get(output));
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(Paths.get(output));
        System.getProperties().remove("input", input);
        System.getProperties().remove("output", output);
    }

    @Test
    public void whenProcessDirectoryThenSuccessfully() throws IOException, NoSuchAlgorithmException {
        // Given
        System.getProperties().put("input", input);
        System.getProperties().put("output", output);

        // When
        Application.main(new String[]{});
        final List<String> resultFileLines = readLines(Paths.get(output));

        // Then
        assertThat(resultFileLines.get(1),
                equalTo("src\\test\\resources\\java\\com\\hexer\\service\\input"));
        assertThat(resultFileLines.get(2),
                equalTo("038cb75743cb1fa080656ec49e66aa378b22166691de045c37d894154ae211eeacb125be762e01c0e295ae60945f9572a9a1bc4715c3e435388cac17f354818b"));
        assertThat(resultFileLines.get(3),
                equalTo("\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\bar"));
        assertThat(resultFileLines.get(4),
                equalTo("\t62b6c74b4b2c6772ac4ae03c66f0548d36dfd492291219ff22f851fdcb358b1bbf703641e4b3f9cf3e45d609711d3a9fd481c83b15841b5632cf27cf6c7a686c"));
        assertThat(resultFileLines.get(5),
                equalTo("\t\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\bar\\fileA.dat"));
        assertThat(resultFileLines.get(6),
                equalTo("\t\taf371785c4fecf30acdd648a7d4d649901eeb67536206a9f517768f0851c0a06616f724b2a194e7bc0a762636c55fc34e0fcaf32f1e852682b2b07a9d7b7a9f9"));
        assertThat(resultFileLines.get(7),
                equalTo("\t\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\bar\\fileB.dat"));
        assertThat(resultFileLines.get(8),
                equalTo("\t\t46868d0a185e942d2fd15739b60096feab4ccdc99139cca4c9db82325606115c8803a6bffe37d6e54c791330add6e1fc861bfa79399f01cc88eed3fcedce13d4"));
        assertThat(resultFileLines.get(9),
                equalTo("\t\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\bar\\fileC.dat"));
        assertThat(resultFileLines.get(10),
                equalTo("\t\tc1e42aa0c8908c9c3d49879a4fc04a59a755735418ddc3a200e911673da188bf46f67818972eac54b38422895391c82b2b0e0cf34aea9468c3ad73c2d0ffa912"));
        assertThat(resultFileLines.get(11),
                equalTo("\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\faz"));
        assertThat(resultFileLines.get(12),
                equalTo("\t9b7728301350a1d2efbd47fe3e65c32ceb596f9524fe9420ca1bea0dfd9cf476a98cc0bf216dcc67984055147b5eb10cac54a3844deeb0b4211ce15366fd88ba"));
        assertThat(resultFileLines.get(13),
                equalTo("\t\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\faz\\fileD.dat"));
        assertThat(resultFileLines.get(14),
                equalTo("\t\t9dd88c920d86ac24112eb692e87b047bb6e69cd413593b009af62a29a71daa68f094dd3340976ae9b8e5d8e5d66d964179409c049103f91f3ccba80d9de63b7a"));
        assertThat(resultFileLines.get(15),
                equalTo("\t\tsrc\\test\\resources\\java\\com\\hexer\\service\\input\\faz\\fileE.dat"));
        assertThat(resultFileLines.get(16),
                equalTo("\t\t40c9964826072dbebe00ea99db34a8c8268088738de8d2a9c02743d0eed36a018adf122bacd789cc569ba2f5f54c75191683e3f252486bf71a5824ae99e20017"));
    }

    private List<String> readLines(final Path rootPath) throws NoSuchAlgorithmException, IOException {
        return Files.readAllLines(rootPath);
    }

}