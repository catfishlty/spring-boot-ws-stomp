package com.example.springbootws.ws;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/14 12:23
 * @email catfish_lty@qq.com
 */
@Data
public class MessagePayload {
    private String path;
    /**
     * @see com.example.springbootws.ws.handler.PayloadType
     * type defined message type
     * 0/null message
     * 1        heartbeat
     * 2        auth
     * 3        setup
     */
    private Integer type;
    private Object data;
}
