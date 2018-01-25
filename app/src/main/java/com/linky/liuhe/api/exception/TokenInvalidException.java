package com.linky.liuhe.api.exception;

/**
 * Created By Linky On 2017-06-07 2:58 PM
 * Token 失效时抛出此异常
 */
public class TokenInvalidException extends RuntimeException {

    public int code;
    public String message;

    public TokenInvalidException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
