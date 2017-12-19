package com.hexer.config;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApplicationConfigTest {

    private ApplicationConfig sut;

    @Test
    public void whenBuildApplicationConfigThenReturnApplicationConfigCaseDefaultParameters() {
        // When
        sut = ApplicationConfig.builder().build();

        // THen
        assertThat(sut.getInputPath(), equalTo("input"));
        assertThat(sut.getOutputPath(), equalTo("output/output.dat"));
        assertThat(sut.getBufferSize(), equalTo(4096));
        assertThat(sut.getDigestAlgorithm(), equalTo("SHA-512"));
    }

    @Test
    public void whenBuildApplicationConfigThenReturnApplicationConfigCaseCustomParameters() {
        // Given
        final String inputPath = "inputPath";
        final String outputPath = "outputPath";
        final String bufferSize = "1024";
        final String digestAlgorithm = "digestAlgorithm";

        // When
        sut = ApplicationConfig.builder()
                .inputPath(inputPath)
                .outputPath(outputPath)
                .bufferSize(bufferSize)
                .digestAlgorithm(digestAlgorithm)
                .build();

        // THen
        assertThat(sut.getInputPath(), equalTo(inputPath));
        assertThat(sut.getOutputPath(), equalTo(outputPath));
        assertThat(sut.getBufferSize(), equalTo(Integer.valueOf(bufferSize)));
        assertThat(sut.getDigestAlgorithm(), equalTo(digestAlgorithm));
    }

    @Test
    public void whenBuildApplicationConfigThenReturnApplicationConfigCaseSystemParameters() {
        // Given
        final String inputPath = "input";
        final String outputPath = "output";
        final String bufferSize = "512";
        final String digestAlgorithm = "digestAlgorithm";

        System.getProperties().put("input", inputPath);
        System.getProperties().put("output", outputPath);
        System.getProperties().put("reader.buffer.size", bufferSize);
        System.getProperties().put("digest.algorithm", digestAlgorithm);

        // When
        sut = ApplicationConfig.builder().build();

        // THen
        assertThat(sut.getInputPath(), equalTo(inputPath));
        assertThat(sut.getOutputPath(), equalTo(outputPath));
        assertThat(sut.getBufferSize(), equalTo(Integer.valueOf(bufferSize)));
        assertThat(sut.getDigestAlgorithm(), equalTo(digestAlgorithm));

        System.getProperties().remove("input");
        System.getProperties().remove("output");
        System.getProperties().remove("reader.buffer.size");
        System.getProperties().remove("digest.algorithm");
    }

}