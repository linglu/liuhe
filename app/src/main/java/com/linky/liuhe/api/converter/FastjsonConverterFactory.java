package com.linky.liuhe.api.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Linky on 5/16/17.
 * fast json 转换器，用于 Retrofit 网络请求
 */
public class FastjsonConverterFactory extends Converter.Factory {

    public static FastjsonConverterFactory create() {
        return new FastjsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastjsonResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastjsonRequestBodyConverter<>();
    }
}