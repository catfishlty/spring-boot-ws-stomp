package com.example.springbootws;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version V1.0 2020/1/7 16:55
 * @email catfish_lty@qq.com
 */
@Controller
@Slf4j
public class MsgController {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/test")
    public String test(String data) {
        return "测试成功";
    }

    /**
     * @return
     * @param:
     * @description 方法描述 根据设置此处应该是 app/hello
     * /topic/getResponse 为订阅
     * @author zhangqiang
     * @date 2019年06月06日 17:03
     */
    @MessageMapping("/hello")
    @SendTo("/topic/getResponse")
    public MsgDTO sub(MsgDTO resp) throws InterruptedException {
        Thread.sleep(1000);
        template.convertAndSend("/topic/getResponse", new MsgDTO("test") );
        return new MsgDTO("感谢您订阅了我");
    }

    @MessageMapping("welcome")
    public String say() throws Exception {
        Thread.sleep(1000);
        return "成功";
    }

    @SubscribeMapping("/message")
    @SendTo("/topic/getResponse")
    public MsgDTO userGetMessage() {
        return new MsgDTO("message");
    }

    @MessageExceptionHandler
    @SendToUser(value = "/errors")
    public MsgDTO handleException(Throwable exception) {
        log.error("",exception);
        return new MsgDTO(exception.getMessage());
    }
}
