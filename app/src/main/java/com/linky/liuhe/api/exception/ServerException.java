package com.linky.liuhe.api.exception;

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}