package com.linky.liuhe.api.response;

import java.util.List;

/**
 * Created By Linky On 2017-05-27 2:47 PM
 * *
 */
public class ListDataResponse<T> {
    public int TotalPage;
    public int TotalNumber;
    public List<T> Rows;
}
