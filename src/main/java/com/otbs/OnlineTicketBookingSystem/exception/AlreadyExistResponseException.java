package com.otbs.OnlineTicketBookingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponseException;

public class AlreadyExistResponseException extends ErrorResponseException {

    private final static HttpStatus STATUS = HttpStatus.CONFLICT;

    public AlreadyExistResponseException(String msg, @Nullable String arg, @Nullable Throwable cause) {
        super(STATUS, cause);
        String detail = msg + arg;
        setDetail(detail);
        setTitle(STATUS.getReasonPhrase());
    }

    public AlreadyExistResponseException(String msg, @Nullable Long arg, @Nullable Throwable cause) {
        this(msg, String.valueOf(arg), cause);
    }

    public AlreadyExistResponseException(String msg, @Nullable Integer arg, @Nullable Throwable cause) {
        this(msg, String.valueOf(arg), cause);
    }

    public AlreadyExistResponseException(String msg, @Nullable Long arg) {
        this(msg, String.valueOf(arg), null);
    }

    public AlreadyExistResponseException(String msg, @Nullable Integer arg) {
        this(msg, String.valueOf(arg), null);
    }

    public AlreadyExistResponseException(String msg, @Nullable String arg) {
        this(msg, arg, null);
    }
}
