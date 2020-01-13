package com.example.springbootws;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/13 18:27
 * @email catfish_lty@qq.com
 */
@Data
public class ResponseResult<T> {
    private Integer code;
    private String desc;
    private T data;
}
