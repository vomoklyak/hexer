package com.hexer;

import com.hexer.config.ApplicationConfig;
import com.hexer.exception.HexerApplicationException;
import com.hexer.provider.DefaultMessageDigestProvider;
import com.hexer.service.digest.DefaultDigestService;
import com.hexer.service.digest.DigestService;
import com.hexer.service.output.AsyncFileOutputWriter;
import com.hexer.service.output.OutputWriter;
import com.hexer.service.processor.RecursiveFileSystemHashProcessor;
import lombok.extern.java.Log;

import java.nio.file.Paths;

@Log
public final class Application {

    public static void main(String[] args) {
        handleException(() -> {
            final ApplicationConfig config = buildApplicationConfig(args);
            final OutputWriter outputWriter = new AsyncFileOutputWriter(config.getOutputPath());
            final DefaultMessageDigestProvider messageDigestProvider =
                    new DefaultMessageDigestProvider(config.getDigestAlgorithm());
            final DigestService digestService =
                    new DefaultDigestService(config.getBufferSize(), messageDigestProvider);

            log.info(String.format("Application started with %s", config));
            final long start = System.currentTimeMillis();
            new RecursiveFileSystemHashProcessor(outputWriter, digestService)
                    .process(Paths.get(config.getInputPath()));

            AsyncFileOutputWriter.class.cast(outputWriter).shutdown();
            final long end = System.currentTimeMillis();
            log.info(String.format("Application successfully finished. Elapsed time: %s ms", (end - start)));
        });
    }

    private static void handleException(final Runnable runnable) {
        try {
            runnable.run();
        } catch (final HexerApplicationException cause) {
            log.info(String.format("ERROR occurred: %s.", cause.getReason()));
            cause.printStackTrace();
        }
    }

    private static ApplicationConfig buildApplicationConfig(final String[] args) {
        final String input = args.length == 1 ? args[0] : null;
        return ApplicationConfig.builder()
                .inputPath(input)
                .build();
    }

}

