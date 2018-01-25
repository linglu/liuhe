package com.linky.liuhe.api;

/**
 * Created by linky on 9/21/17.
 */

public class RequestBody<T> {

    public String key;
    public T var;
    public String currentPage = null;
    public String offset = null;

    public RequestBody(String key, T var) {
        this.key = key;
        this.var = var;
    }

    public RequestBody(String key, T var, int currentPage, int offset) {
        this.key = key;
        this.var = var;
        this.currentPage = currentPage + "";
        this.offset = offset + "";
    }
}
