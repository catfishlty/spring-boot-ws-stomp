package com.example.springbootws;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Catfish
 * @version V1.0 2020/1/7 16:22
 * @email catfish_lty@qq.com
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册stomp端点，主要是起到连接作用
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
            .addEndpoint("/ws")  //端点名称
            //.setHandshakeHandler() 握手处理，主要是连接的时候认证获取其他数据验证等
            .setHandshakeHandler(new HandshakeHandler() {
                @Override
                public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {
//                    List<String> dingCodeList = request.getHeaders().get("dingcode");
//                    if(Objects.isNull(dingCodeList) || dingCodeList.size()!=1){
//                        throw new HandshakeFailureException("dingCode is missing");
//                    }
                    return true;
                }
            })
            .addInterceptors(new HandshakeInterceptor() {
                @Override
                public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                    return true;
                }

                @Override
                public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                }
            }) //拦截处理，和http拦截类似
            .setAllowedOrigins("*") //跨域
            ; //使用sockJS

    }

    /**
     * 注册相关服务
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //这里使用的是内存模式，生产环境可以使用rabbitmq或者其他mq。
        //这里注册两个，主要是目的是将广播和队列分开。
        //registry.enableStompBrokerRelay().setRelayHost().setRelayPort() 其他方式
        registry.enableSimpleBroker("/topic", "/user");
        //设置客户端前缀 即@MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
        //点对点发送前缀
        registry.setUserDestinationPrefix("/user");
    }
}

