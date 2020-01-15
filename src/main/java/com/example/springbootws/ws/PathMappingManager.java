package com.example.springbootws.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootws.lib.StringConstants;
import com.example.springbootws.ws.anno.MessageController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version V1.0 2020/1/14 10:10
 * @email catfish_lty@qq.com
 */
@Slf4j
public class PathMappingManager {

    private final Map<String, Wrapper> m;

    public PathMappingManager(ApplicationContext context) {
        m = new HashMap<>();
        init(context);
    }

    public Object invoke(String path, Object... args) throws InvocationTargetException, IllegalAccessException {
        Wrapper wrapper = m.get(path);
        if (Objects.isNull(wrapper)) {
            log.error("no handler for path {}", path);
            throw new IllegalArgumentException("no handler for path " + path);
        }
        return wrapper.getMethod().invoke(wrapper.obj, args);
    }

    public Wrapper get(String k) {
        return m.get(k);
    }

    public int size() {
        return m.size();
    }

    public void init(ApplicationContext context) {
        Map<String, Object> clzs = context.getBeansWithAnnotation(MessageController.class);
        for (Object obj : clzs.values()) {
            Class<?> clz = obj.getClass();
            RequestMapping majorPathAnnotation = clz.getAnnotation(RequestMapping.class);
            if (majorPathAnnotation.value().length <= 0) {
                log.warn("{} is not defined ws interface", clz.getName());
                continue;
            }
            String majorPath = trimPath(majorPathAnnotation.value()[0]);
            for (Method method : clz.getMethods()) {
                MessageMapping minorPathAnnotation = method.getAnnotation(MessageMapping.class);
                if (Objects.isNull(minorPathAnnotation)) {
                    continue;
                }
                String[] minorPath = minorPathAnnotation.value();
                if (minorPath.length == 0) {
                    minorPath = new String[]{""};
                }
                for (String mp : minorPath) {
                    String fullPath = trimPath(majorPath + StringConstants.DOT + trimPath(mp));
                    if (m.containsKey(fullPath)) {
                        log.error("Message Path {} conflict", fullPath);
                        throw new IllegalArgumentException("Message Path " + fullPath + " conflict");
                    }
                    m.put(fullPath, new PathMappingManager.Wrapper(obj, method));
                }
            }
        }
        log.info("path handler size {}", m.size());
    }

    private String trimPath(String str) {
        String t = StringUtils.strip(str, "./");
        return t == null ? "" : t;
    }

    @Data
    @AllArgsConstructor
    public static class Wrapper {
        private Object obj;
        private Method method;
    }
}
