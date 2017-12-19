package com.hexer.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@code DefaultExecutorProvider} provides possibility to obtain {@link java.util.concurrent.ExecutorService}.
 */
public class DefaultExecutorProvider implements ExecutorProvider {

    private final ExecutorService executor;

    public DefaultExecutorProvider(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize, Thread::new);
    }

    @Override
    public ExecutorService provide() {
        return executor;
    }

}
