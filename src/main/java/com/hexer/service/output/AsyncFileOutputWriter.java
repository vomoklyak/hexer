package com.hexer.service.output;

import com.hexer.exception.HexerApplicationException;
import com.hexer.provider.DefaultExecutorProvider;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.hexer.exception.Reason.OUTPUT_WRITER_EXCEPTION;

/**
 * {@code AsyncFileOutputWriter} provides possibility to write path-hash pairs in file asynchronously.
 * Class is built over RandomAccessFile, that is why it is possible to write path and later write hash.
 * Maximum hash size is 128 bits.
 */
public class AsyncFileOutputWriter implements OutputWriter {

    private static final int HASH_SIZE = 128;
    private static final byte TAB = "\t".getBytes()[0];
    private static final byte[] NEW_LINE = "\n".getBytes();

    private final RandomAccessFile randomAccessFile;
    private final Map<String, Long> pathToHashPosition;
    private final ExecutorService executorService;

    private long endOfFilePosition;

    public AsyncFileOutputWriter(final String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
            this.randomAccessFile = new RandomAccessFile(path, "rw");
        } catch (final IOException cause) {
            throw new HexerApplicationException(cause, OUTPUT_WRITER_EXCEPTION);
        }
        this.pathToHashPosition = new HashMap<>();
        this.executorService = new DefaultExecutorProvider(1).provide();
    }

    public void write(final String path, final int padding) {
        executorService.execute(() -> {
            try {
                randomAccessFile.seek(endOfFilePosition);
                startNewLine(padding);
                randomAccessFile.write(path.getBytes());
                startNewLine(padding);
                long hashPosition = getCurrentPosition();
                pathToHashPosition.put(path, hashPosition);
                randomAccessFile.seek(hashPosition + HASH_SIZE);
                setEndOfFilePosition(hashPosition + HASH_SIZE);
            } catch (final IOException cause) {
                throw new HexerApplicationException(cause, OUTPUT_WRITER_EXCEPTION);
            }
        });
    }

    public void write(final String path, final String hash, final int padding) {
        executorService.execute(() -> {
            try {
                randomAccessFile.seek(endOfFilePosition);
                startNewLine(padding);
                randomAccessFile.write(path.getBytes());
                startNewLine(padding);
                randomAccessFile.write(hash.getBytes());
                setEndOfFilePosition(getCurrentPosition());
            } catch (final IOException cause) {
                throw new HexerApplicationException(cause, OUTPUT_WRITER_EXCEPTION);
            }
        });
    }

    public void writeHash(final String path, final String hash) {
        executorService.execute(() -> {
            try {
                randomAccessFile.seek(pathToHashPosition.get(path));
                randomAccessFile.write(hash.getBytes());
                pathToHashPosition.remove(path);
            } catch (final IOException cause) {
                throw new HexerApplicationException(cause, OUTPUT_WRITER_EXCEPTION);
            }
        });
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            randomAccessFile.close();
        } catch (IOException | InterruptedException cause) {
            throw new HexerApplicationException(cause, OUTPUT_WRITER_EXCEPTION);
        }
    }

    private void startNewLine(final int padding) throws IOException {
        randomAccessFile.write(NEW_LINE);
        randomAccessFile.write(padding(padding));
    }

    private byte[] padding(int tabAmount) {
        final byte[] tabs = new byte[tabAmount];
        Arrays.fill(tabs, TAB);

        return tabs;
    }

    private long getCurrentPosition() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    private void setEndOfFilePosition(final long currentPosition) {
        if (endOfFilePosition < currentPosition) {
            endOfFilePosition = currentPosition;
        }
    }

}
