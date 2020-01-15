package com.example.springbootws.ws.anno;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * @author Catfish
 * @version V1.0 2020/1/15 12:04
 * @email catfish_lty@qq.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
