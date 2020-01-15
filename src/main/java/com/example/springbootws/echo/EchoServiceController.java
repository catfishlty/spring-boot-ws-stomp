package com.example.springbootws.echo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootws.MsgDTO;
import com.example.springbootws.ws.MessageResponseResult;
import com.example.springbootws.ws.anno.MessageController;
import com.example.springbootws.ws.anno.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 */
@MessageController
@RequestMapping("echo")
@Slf4j
public class EchoServiceController{

    @MessageMapping(value = "test1")
    public MessageResponseResult<String> sendMsg(@Session Object session, @Payload MsgDTO data) {
        log.info("income {}",data);
        MessageResponseResult<String> res = new MessageResponseResult<>();
        res.setCode(1000);
        res.setData("hello test1");
        return res;
    }

    @MessageMapping(value = "test2")
    public void test2(@Payload Object data) {
        log.info("income {}",data);
        MessageResponseResult<String> res = new MessageResponseResult<>();
        res.setCode(1000);
        res.setData("hello test2");
        log.info("{} {}", data, res);
    }
}
