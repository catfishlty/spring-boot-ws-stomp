package com.example.springbootws;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.example.springbootws.echo.DefaultEchoService;
import com.example.springbootws.echo.EchoService;
import com.example.springbootws.echo.EchoWebSocketHandler;

/**
 * @author Catfish
 * @version V1.0 2020/1/10 12:01
 * @email catfish_lty@qq.com
 */
@Configuration
@EnableAutoConfiguration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler(echoService()), "/ws").setAllowedOrigins("*");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public EchoWebSocketHandler echoWebSocketHandler(EchoService echoService) {
        return new EchoWebSocketHandler(echoService);
    }

    @Bean
    public EchoService echoService() {
        return new DefaultEchoService("Did you say \"%s\"?");
    }
}
