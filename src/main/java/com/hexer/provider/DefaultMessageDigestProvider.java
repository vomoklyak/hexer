package com.hexer.provider;

import com.hexer.exception.HexerApplicationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.AllArgsConstructor;

import static com.hexer.exception.Reason.DIGEST_ALGORITHM_EXCEPTION;

/**
 * {@code DefaultMessageDigestProvider} provides possibility to obtain {@link java.security.MessageDigest}.
 * Class is thread safe.
 */
@AllArgsConstructor
public class DefaultMessageDigestProvider
        extends ThreadLocal<MessageDigest> implements MessageDigestProvider {

    private final String algorithm;

    @Override
    public MessageDigest provide() {
        return get();
    }

    @Override
    protected MessageDigest initialValue() {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException cause) {
            throw new HexerApplicationException(cause, DIGEST_ALGORITHM_EXCEPTION);
        }
    }

}
