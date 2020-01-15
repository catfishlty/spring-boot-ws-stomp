package com.example.springbootws.lib;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Catfish
 * @version 1.0 2018-11-01 15:31:42
 * @email catfish_lty@qq.com
 **/
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    private void setContext(ApplicationContext ctx) {
        context = ctx;
    }
}
