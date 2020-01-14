package com.example.springbootws.echo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootws.MsgDTO;
import com.example.springbootws.ResponseResult;
import com.example.springbootws.Session;

import lombok.extern.slf4j.Slf4j;

@Component
@RequestMapping("echo")
@Slf4j
public class EchoServiceController{

    @MessageMapping(value = "test1")
    public ResponseResult<String> sendMsg(@Session Object session, @Payload MsgDTO data) {
        log.info("income {}",data);
        ResponseResult<String> res = new ResponseResult<>();
        res.setCode(1000);
        res.setData("hello test1");
        return res;
    }

    @MessageMapping(value = "test2")
    public void test2(@Payload Object data) {
        log.info("income {}",data);
        ResponseResult<String> res = new ResponseResult<>();
        res.setCode(1000);
        res.setData("hello test2");
        log.info("{} {}", data, res);
    }
}
