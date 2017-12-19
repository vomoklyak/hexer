package com.hexer.service.processor;

import com.hexer.service.digest.DigestService;
import com.hexer.service.output.OutputWriter;
import com.hexer.util.HexUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.Paths.get;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecursiveFileSystemHashProcessorTest {

    private OutputWriter outputWriter;
    private DigestService digestService;
    private RecursiveFileSystemHashProcessor sut;

    @Before
    public void before() {
        outputWriter = Mockito.mock(OutputWriter.class);
        digestService = Mockito.mock(DigestService.class);
        sut = new RecursiveFileSystemHashProcessor(outputWriter, digestService);
    }

    @Test
    public void whenProcessFileThenDigestAndWriteToFile() throws NoSuchAlgorithmException, IOException {
        // Given
        final Path path = get("src/test/resources/java/com/hexer/service/processor/input/fileA.dat");
        final byte[] hash = hash(path);
        when(digestService.digest(any(File.class))).thenReturn(hash);

        // When
        sut.process(path);

        // Then
        verify(digestService, times(1)).digest(any(File.class));
        verify(outputWriter, times(1)).write(path.toString(), HexUtil.toHex(hash), 0);
    }

    @Test
    public void whenProcessDirectoryThenDigestAndWriteToFile() throws NoSuchAlgorithmException, IOException {
        // Given
        final Path directoryPath = get("src/test/resources/java/com/hexer/service/processor/input/");
        final Path filePath = get("src/test/resources/java/com/hexer/service/processor/input/fileA.dat");

        final byte[] fileHash = hash(filePath);

        when(digestService.digest(any(File.class))).thenReturn(fileHash);
        when(digestService.digest(any(byte[][].class))).thenReturn(fileHash);

        // When
        sut.process(directoryPath);

        // Then
        verify(digestService, times(1)).digest(any(File.class));
        verify(digestService, times(1)).digest(any(byte[][].class));
        verify(outputWriter, times(1)).write(filePath.toString(), HexUtil.toHex(fileHash), 1);
        verify(outputWriter, times(1)).write(directoryPath.toString(), 0);
        verify(outputWriter, times(1)).writeHash(directoryPath.toString(), HexUtil.toHex(fileHash));
    }

    private byte[] hash(final Path rootPath) throws NoSuchAlgorithmException, IOException {
        return MessageDigest.getInstance("SHA-512")
                .digest(Files.readAllBytes(rootPath));
    }

}