package com.chaoji.other.kongzue.baseokhttp.exceptions;

public class TimeOutException extends Exception {
    public TimeOutException(){
        super("请求超时");
    }
}