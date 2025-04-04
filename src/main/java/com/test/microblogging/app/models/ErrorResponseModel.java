package com.test.microblogging.app.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseModel {
    private String errorCode;
    private String errorMessage;
    private String details;

    // Constructor
    public ErrorResponseModel(String errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }

}
