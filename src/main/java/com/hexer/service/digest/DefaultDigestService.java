package com.hexer.service.digest;

import com.hexer.exception.HexerApplicationException;
import com.hexer.provider.MessageDigestProvider;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;

import static com.hexer.exception.Reason.DIGEST_CALCULATION_EXCEPTION;
import static java.lang.Math.min;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/**
 * {@code DefaultDigestService} provides possibility to calculate hash (digest).
 */
@AllArgsConstructor
public class DefaultDigestService implements DigestService {

    private final int bufferSize;
    private final MessageDigestProvider messageDigestProvider;

    @Override
    public byte[] digest(final File file) {
        assert file != null;

        final MessageDigest messageDigest = getMessageDigest();
        try (final FileChannel fileChannel = new FileInputStream(file).getChannel()) {
            final MappedByteBuffer mappedByteBuffer =
                    fileChannel.map(READ_ONLY, 0L, fileChannel.size());
            byte[] buffer = new byte[bufferSize];
            int length;
            while (mappedByteBuffer.hasRemaining()) {
                length = min(mappedByteBuffer.remaining(), bufferSize);
                mappedByteBuffer.get(buffer, 0, length);
                messageDigest.update(buffer, 0, length);
            }

            return messageDigest.digest();
        } catch (IOException cause) {
            throw new HexerApplicationException(cause, DIGEST_CALCULATION_EXCEPTION);
        }
    }

    @Override
    public byte[] digest(final byte[][] byteArrays) {
        assert byteArrays != null;

        final MessageDigest messageDigest = getMessageDigest();
        Arrays.stream(byteArrays).forEach(messageDigest::update);

        return messageDigest.digest();
    }

    private MessageDigest getMessageDigest() {
        final MessageDigest messageDigest = messageDigestProvider.provide();
        messageDigest.reset();

        return messageDigest;
    }

}
