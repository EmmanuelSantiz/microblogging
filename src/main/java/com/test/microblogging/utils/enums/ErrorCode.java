package com.test.microblogging.utils.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SERVICE_UNAVAILABLE("1001", "Servicio no disponible"),
    DATA_INSERTION_ERROR("1002", "Ocurrió un error al agregar datos"),
    DATA_NOT_FOUND("1003", "Recurso no encontrado"),
    INTERNAL_SERVER_ERROR("1005", "Error interno en el servidor"),
    INVALID_REQUEST("1006", "Solicitud inválida");

    private final String code;
    private final String message;

    // Constructor del enum
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
