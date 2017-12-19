package com.hexer.exception;

import lombok.Getter;

/**
 * {@code HexerApplicationException} is basic exception for application.
 * Exception contains field {@link com.hexer.exception.Reason } that describes the cause.
 */
@Getter
public class HexerApplicationException extends RuntimeException {

    private final Reason reason;

    public HexerApplicationException(final Throwable cause, final Reason reason) {
        super(cause);
        this.reason = reason;
    }

}
