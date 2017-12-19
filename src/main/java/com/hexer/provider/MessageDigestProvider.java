package com.hexer.provider;

import java.security.MessageDigest;

/**
 * {@code MessageDigestProvider} interface defines method for obtaining {@link java.security.MessageDigest}.
 */
public interface MessageDigestProvider {

    /**
     * Provide MessageDigest.
     */
    MessageDigest provide();

}
