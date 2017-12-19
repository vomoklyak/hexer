package com.hexer.service.processor;

import java.nio.file.Path;


/**
 * {@code FileSystemHashProcessor} interface defines method for file system hash processing.
 */

public interface FileSystemHashProcessor {

    /**
     * Process path.
     *
     * @param rootPath defines root path
     */
    void process(final Path rootPath);

}
