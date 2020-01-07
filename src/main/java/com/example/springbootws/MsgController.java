package com.example.springbootws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Catfish
 * @version V1.0 2020/1/7 16:55
 * @email catfish_lty@qq.com
 */
@RestController
public class MsgController {
    @MessageMapping("/test")
    public String test(String data) {
        return "测试成功";
    }

    /**
     * @param:
     * @return
     * @description 方法描述 根据设置此处应该是 app/hello
     * /topic/getResponse 为订阅
     * @author zhangqiang
     * @date 2019年06月06日 17:03
     */
    @MessageMapping("/hello")
    @SendTo("/topic/getResponse")
    public String sub() throws InterruptedException {
        Thread.sleep(1000);
        return "感谢您订阅了我";
    }

    @MessageMapping("welcome")
    public String say() throws Exception {
        Thread.sleep(1000);
        return "成功";
    }

    @MessageMapping("/message")
    @SendToUser("/message")
    public String userGetMessage() {
        return "message";

    }

}
