package com.hexer.provider;

import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultExecutorProviderTest {

    private DefaultExecutorProvider sut;

    @Test
    public void whenProvideExecutorThenReturnExecutor() {
        // Given
        sut = new DefaultExecutorProvider(1);

        // When
        final ExecutorService result = sut.provide();

        // Then
        assertThat(result, notNullValue());
    }

}