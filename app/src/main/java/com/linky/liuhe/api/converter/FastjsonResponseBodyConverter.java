package com.linky.liuhe.api.converter;

import com.alibaba.fastjson.JSON;
import com.linky.liuhe.utils.L;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Linky on 5/16/17.
 *
 */
public class FastjsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;

    public FastjsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        L.json(response);
        return JSON.parseObject(response, type);
    }
}