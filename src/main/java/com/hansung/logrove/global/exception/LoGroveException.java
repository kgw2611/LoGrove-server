package com.hansung.logrove.global.exception;

import lombok.Getter;

@Getter
public class LoGroveException extends RuntimeException {

    private final ErrorCode errorCode;

    public LoGroveException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}