package com.hexer.provider;

import java.util.concurrent.ExecutorService;

/**
 * {@code ExecutorProvider} interface defines method for obtaining {@link java.util.concurrent.ExecutorService}.
 */
public interface ExecutorProvider {

    /**
     * Provide ExecutorService.
     */
    ExecutorService provide();

}
