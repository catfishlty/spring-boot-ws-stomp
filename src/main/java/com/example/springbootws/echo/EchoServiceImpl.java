package com.example.springbootws.echo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootws.ResponseResult;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("echo")
@Slf4j
public class EchoServiceImpl implements EchoService {

    private final String echoFormat;

    public EchoServiceImpl(String echoFormat) {
        this.echoFormat = (echoFormat != null) ? echoFormat : "%s";
    }

    @Override
    public String getMessage(String message) {
        return String.format(this.echoFormat, message);
    }

    @MessageMapping(value = "test1")
    public ResponseResult<String> sendMsg(@Payload Object data) {
        ResponseResult<String> res = new ResponseResult<>();
        res.setCode(1000);
        res.setData("hello");
        return res;
    }

    @MessageMapping(value = "test2")
    public void test2(@Payload Object data) {
        ResponseResult<String> res = new ResponseResult<>();
        res.setCode(1000);
        res.setData("hello");
        log.info("{} {}", data, res);
    }
}
