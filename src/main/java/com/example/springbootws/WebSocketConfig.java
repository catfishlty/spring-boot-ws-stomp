package com.example.springbootws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.example.springbootws.echo.MajorWebSocketHandler;
import com.example.springbootws.message.mapping.PathMappingManager;

/**
 * @author Catfish
 * @version V1.0 2020/1/10 12:01
 * @email catfish_lty@qq.com
 */
@Configuration
@EnableWebSocket
//@DependsOn("majorWebSocketHandler")
public class WebSocketConfig implements WebSocketConfigurer {
    private final MajorWebSocketHandler handler;

    public WebSocketConfig(MajorWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws").setAllowedOrigins("*");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
