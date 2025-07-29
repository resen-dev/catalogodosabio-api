package com.f1rstdigital.catalogodosabio.handler.exceptions;

import com.f1rstdigital.catalogodosabio.handler.CustomRuntimeException;
import com.f1rstdigital.catalogodosabio.handler.ErrorTypeEnum;

import java.util.UUID;

public class RecordNotFoundException extends CustomRuntimeException {
    public RecordNotFoundException(Class<?> entity, UUID id) {
        super(ErrorTypeEnum.RECORD_NOT_FOUND_EXCEPTION, "%s - ID: %s not found".formatted(entity.getSimpleName().toUpperCase(), id.toString()));
    }
}
