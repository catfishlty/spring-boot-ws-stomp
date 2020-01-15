package com.example.springbootws.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootws.ws.PathMappingManager;
import com.example.springbootws.ws.handler.MainWebSocketHandler;
import com.example.springbootws.ws.session.SessionManager;

/**
 * @author Catfish
 * @version V1.0 2020/1/10 12:01
 * @email catfish_lty@qq.com
 */
@Configuration
public class WebSocketManagerConfig {
    @Bean
    public MainWebSocketHandler webSocketHandler(PathMappingManager pathMappingManager, SessionManager sessionManager) {
        return new MainWebSocketHandler(pathMappingManager, sessionManager);
    }

    @Bean
    public PathMappingManager pathMappingManager(ApplicationContext context) {
        return new PathMappingManager(context);
    }

    @Bean
    public SessionManager sessionManager() {
        return new SessionManager();
    }
}
