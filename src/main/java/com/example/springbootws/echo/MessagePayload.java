package com.example.springbootws.echo;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/14 12:23
 * @email catfish_lty@qq.com
 */
@Data
public class MessagePayload {
    private String path;
    private Object data;
}
