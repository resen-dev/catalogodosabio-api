package com.f1rstdigital.catalogodosabio.handler;

import org.springframework.http.HttpStatus;

public class CustomRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String customMessage;

    public CustomRuntimeException(ErrorTypeEnum errorTypeEnum) {
        super(errorTypeEnum.getMessage());
        this.httpStatus = errorTypeEnum.getHttpStatus();
        this.customMessage = errorTypeEnum.getMessage();
    }

    public CustomRuntimeException(ErrorTypeEnum errorTypeEnum, String customMessage) {
        super(errorTypeEnum.getMessage());
        this.httpStatus = errorTypeEnum.getHttpStatus();
        this.customMessage = customMessage;
    }


    public String getCustomMessage() {
        return customMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
