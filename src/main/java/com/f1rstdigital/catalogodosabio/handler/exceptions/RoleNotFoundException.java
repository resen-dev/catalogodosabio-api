package com.f1rstdigital.catalogodosabio.handler.exceptions;

import com.f1rstdigital.catalogodosabio.handler.CustomRuntimeException;
import com.f1rstdigital.catalogodosabio.handler.ErrorTypeEnum;

public class RoleNotFoundException extends CustomRuntimeException {
    public RoleNotFoundException(String role) {
        super(ErrorTypeEnum.ROLE_NOT_FOUND_EXCEPTION, "Role: %s not found".formatted(role));
    }
}
