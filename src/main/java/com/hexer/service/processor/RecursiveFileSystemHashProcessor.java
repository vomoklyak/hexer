package com.hexer.service.processor;

import com.hexer.exception.HexerApplicationException;
import com.hexer.service.digest.DigestService;
import com.hexer.service.output.OutputWriter;
import com.hexer.util.HexUtil;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hexer.exception.Reason.FILE_SYSTEM_PROCESSOR_EXCEPTION;
import static java.util.Comparator.comparing;

/**
 * {@code RecursiveFileSystemHashProcessor} provides possibility to process path hash recursively.
 * Algorithm - recursion in depth.
 */
@AllArgsConstructor
public class RecursiveFileSystemHashProcessor implements FileSystemHashProcessor {

    private OutputWriter outputWriter;
    private DigestService digestService;

    public void process(final Path rootPath) {
        calculateHash(rootPath, rootPath);
    }

    private byte[] calculateHash(final Path path, final Path rootPath) {
        final File file = path.toFile();
        final String pathStr = file.toString();
        final int padding = path.getNameCount() - rootPath.getNameCount();

        if (file.isDirectory()) {
            try {
                outputWriter.write(pathStr, padding);
                final byte[][] fileHashes = Files.list(path)
                        .sorted(comparing(Path::getFileName))
                        .map(currentPath -> calculateHash(currentPath, rootPath))
                        .toArray(byte[][]::new);

                final byte[] directoryHash = digestService.digest(fileHashes);
                outputWriter.writeHash(pathStr, HexUtil.toHex(directoryHash));
                return directoryHash;
            } catch (final IOException cause) {
                throw new HexerApplicationException(cause, FILE_SYSTEM_PROCESSOR_EXCEPTION);
            }
        } else {
            final byte[] fileHash = digestService.digest(file);
            outputWriter.write(pathStr, HexUtil.toHex(fileHash), padding);
            return fileHash;
        }
    }

}
