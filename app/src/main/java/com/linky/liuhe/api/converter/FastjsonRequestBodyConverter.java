package com.linky.liuhe.api.converter;

import com.alibaba.fastjson.JSON;
import com.linky.liuhe.utils.L;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by linky on 5/16/17.
 *
 */
public class FastjsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(T value) throws IOException {
        String body = JSON.toJSONString(value);
        L.json(body);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
    }
}