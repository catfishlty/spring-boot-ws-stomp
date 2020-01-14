package com.example.springbootws;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootws.echo.MajorWebSocketHandler;
import com.example.springbootws.message.mapping.PathMappingManager;

/**
 * @author Catfish
 * @version V1.0 2020/1/10 12:01
 * @email catfish_lty@qq.com
 */
@Configuration
public class WebSocketManagerConfig {
    @Bean
    public MajorWebSocketHandler webSocketHandler(PathMappingManager pathMappingManager) {
        return new MajorWebSocketHandler(pathMappingManager);
    }

    @Bean
    public PathMappingManager pathMappingManager(ApplicationContext context) {
        return new PathMappingManager(context);
    }
}
