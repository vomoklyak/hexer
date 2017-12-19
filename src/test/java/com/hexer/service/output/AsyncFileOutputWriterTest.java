package com.hexer.service.output;

import com.hexer.exception.HexerApplicationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class AsyncFileOutputWriterTest {

    private final String EMPTY_STRING = "";
    private final String path = "src/test/resources/java/com/hexer/service/output/output.dat";
    private final String hash = "9dd88c920d86ac24112eb692e87b047bb6e69cd413593b009af62a29a71daa68f094dd3340976ae9b8e5d8e5d66d964179409c049103f91f3ccba80d9de63b7a";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private AsyncFileOutputWriter sut;

    @Before
    public void before() throws IOException {
        Files.deleteIfExists(get(path));
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(get(path));
    }

    @Test
    public void whenWritePathThenWritePathToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, 0);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), equalTo(path));
    }

    @Test
    public void whenWritePathAndHashThenWritePathAndHashToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, hash, 0);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), equalTo(path));
        assertThat(fileLines.get(2), equalTo(hash));
    }

    @Test
    public void whenWriteHashThenWriteHashToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, 0);
        sut.writeHash(path, hash);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), equalTo(path));
        assertThat(fileLines.get(2), equalTo(hash));
    }

    @Test
    public void whenWritePathWithTabThenWritePathWithTabToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, 1);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), startsWith("\t"));
    }

    @Test
    public void whenWritePathWithHashWitTabThenWritePathWithHashWitTabToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, hash, 1);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), startsWith("\t"));
        assertThat(fileLines.get(2), startsWith("\t"));
    }

    @Test
    public void whenWriteHashWitTabThenWriteHashWithTabToFile() throws IOException {
        // Given
        sut = new AsyncFileOutputWriter(path);

        // When
        sut.write(path, 1);
        sut.writeHash(path, hash);
        sut.shutdown();
        final List<String> fileLines = Files.readAllLines(get(path));

        // Then
        assertThat(fileLines.get(0), equalTo(EMPTY_STRING));
        assertThat(fileLines.get(1), startsWith("\t"));
        assertThat(fileLines.get(2), startsWith("\t"));
    }

    @Test
    public void whenNonexistentPathThenException() throws IOException {
        // Given
        final String nonexistentPath = "\\nonexistent\\nonexistentFile.dat";

        // When
        expectedException.expect(HexerApplicationException.class);
        new AsyncFileOutputWriter(nonexistentPath);
    }

}