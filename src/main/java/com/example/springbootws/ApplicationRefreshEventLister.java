package com.example.springbootws;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootws.message.mapping.PathMappingManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version V1.0 2020/1/14 11:38
 * @email catfish_lty@qq.com
 */
@Slf4j
public class ApplicationRefreshEventLister implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
