package com.linky.liuhe.api.response;

/**
 * Created By Linky On 2017-05-23 8:31 PM
 * *
 */
public class Response<T> {
    public String ResultCode;       // "true" or "false"
    public String ResultDesc;       // 描述信息
    public T result;
}
