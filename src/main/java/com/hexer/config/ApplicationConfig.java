package com.hexer.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * {@code ApplicationConfig} represents application config.
 * Provides possibility to set configuration parameters as builder parameters, or system variables.
 * Class also defines default values:
 * inputPath = input
 * outputPath = output/output.dat
 * bufferSize = 4096
 * digestAlgorithm = SHA-512
 */
@Getter
@ToString
public final class ApplicationConfig {

    private final String inputPath;
    private final String outputPath;
    private final String bufferSize;
    private final String digestAlgorithm;

    @Builder
    private ApplicationConfig(String inputPath,
                              String outputPath,
                              String bufferSize,
                              String digestAlgorithm) {
        this.inputPath =
                initiateParam("input", inputPath, "input");
        this.outputPath =
                initiateParam("output", outputPath, "output/output.dat");
        this.bufferSize =
                initiateParam("reader.buffer.size", bufferSize, "4096");
        this.digestAlgorithm =
                initiateParam("digest.algorithm", digestAlgorithm, "SHA-512");
    }

    private String initiateParam(String paramName, String paramValue, String defaultParamValue) {
        return paramValue != null ? paramValue :
                String.class.cast(System.getProperties().getOrDefault(paramName, defaultParamValue));
    }

    public int getBufferSize() {
        return Integer.valueOf(bufferSize);
    }

}
