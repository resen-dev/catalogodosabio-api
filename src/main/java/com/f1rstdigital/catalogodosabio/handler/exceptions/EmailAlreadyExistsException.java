package com.f1rstdigital.catalogodosabio.handler.exceptions;

import com.f1rstdigital.catalogodosabio.handler.CustomRuntimeException;
import com.f1rstdigital.catalogodosabio.handler.ErrorTypeEnum;

public class EmailAlreadyExistsException extends CustomRuntimeException {
    public EmailAlreadyExistsException() {
        super(ErrorTypeEnum.EMAIL_ALREADY_EXISTS_EXCEPTION);
    }
}
