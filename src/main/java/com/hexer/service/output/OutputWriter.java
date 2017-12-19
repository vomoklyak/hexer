package com.hexer.service.output;

/**
 * {@code OutputWriter} interface defines methods for writing key-value (path-hash) pairs with padding.
 */
public interface OutputWriter {

    /**
     * Write only path with padding.
     *
     * @param path    defines path
     * @param padding defines padding in tabs
     */
    void write(String path, int padding);

    /**
     * Write path and hash with padding.
     *
     * @param path    defines path
     * @param hash    defines hash
     * @param padding defines padding in tabs
     */
    void write(String path, String hash, int padding);

    /**
     * Write path and hash.
     *
     * @param path defines path
     * @param hash defines hash
     */
    void writeHash(String path, String hash);

}
