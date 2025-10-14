package com.box.other.kongzue.baseokhttp.exceptions;

public class SameRequestException extends Exception {
    public SameRequestException(String errorInfo) {
        super(errorInfo);
    }
}