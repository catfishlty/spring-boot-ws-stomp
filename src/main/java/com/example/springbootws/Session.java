package com.example.springbootws;

import java.lang.annotation.*;

/**
 * @author Catfish
 * @version V1.0 2020/1/14 15:32
 * @email catfish_lty@qq.com
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Session {
}
