package com.f1rstdigital.catalogodosabio.handler;

import org.springframework.http.HttpStatus;

public enum ErrorTypeEnum {

    EMAIL_ALREADY_EXISTS_EXCEPTION("The provided email already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND_EXCEPTION("Role was not found.", HttpStatus.NOT_FOUND),
    RECORD_NOT_FOUND_EXCEPTION("Record was not found", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorTypeEnum(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
