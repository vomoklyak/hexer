package com.hexer.service.digest;

import java.io.File;

/**
 * {@code DigestService} interface defines methods for hash calculation (digest).
 * Hash is byte array.
 */
public interface DigestService {

    /**
     * Calculate hash for file.
     *
     * @param file defines file
     */
    byte[] digest(File file);

    /**
     * Calculate total hash for hash array.
     *
     * @param byteArrays defines hash array
     */
    byte[] digest(byte[][] byteArrays);

}
