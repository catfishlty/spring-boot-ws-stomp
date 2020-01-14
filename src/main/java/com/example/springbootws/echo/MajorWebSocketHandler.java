/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.springbootws.echo;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.springbootws.JacksonUtil;
import com.example.springbootws.Session;
import com.example.springbootws.message.mapping.PathMappingManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
@Slf4j
public class MajorWebSocketHandler extends TextWebSocketHandler {

    private final PathMappingManager m;

    public MajorWebSocketHandler(PathMappingManager m) {
        this.m = m;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New Connection {} {} {}", session, session.getHandshakeHeaders(), session.getAttributes());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessagePayload payload = JacksonUtil.jsonToObject(message.getPayload(), MessagePayload.class);
        if (Objects.isNull(payload)) {
            throw new IllegalArgumentException("payload format is unavailable");
        }
        PathMappingManager.Wrapper wrapper = m.get(payload.getPath());
        Object[] params = new Object[wrapper.getMethod().getParameterCount()];
        Annotation[][] anno = wrapper.getMethod().getParameterAnnotations();
        boolean payloadFlag = false;
        boolean sessionFlag = false;
        for (int i = 0; i < anno.length; i++) {
            Set<Annotation> ps = Arrays.stream(anno[i])
                .filter(annotation -> Payload.class.equals(annotation.annotationType()))
                .collect(Collectors.toSet());
            Set<Annotation> ss = Arrays.stream(anno[i])
                .filter(annotation -> Session.class.equals(annotation.annotationType()))
                .collect(Collectors.toSet());
            if (ps.size() > 0) {
                if (payloadFlag) {
                    throw new IllegalArgumentException("method " + wrapper.getMethod().getName() + "has duplicated @Payload parameters");
                }
                Class<?> paramClz = wrapper.getMethod().getParameterTypes()[i];
                params[i] = JacksonUtil.mapToBean(payload.getData(),paramClz);
                payloadFlag = true;
            }
            if (ss.size() > 0) {
                if (sessionFlag) {
                    throw new IllegalArgumentException("method " + wrapper.getMethod().getName() + "has duplicated @Session parameters");
                }
                //TODO setSession
                params[i] = null;
                sessionFlag = true;
            }
        }
        Object response = m.invoke(payload.getPath(), params);
        if (Objects.nonNull(response)){
            session.sendMessage(new TextMessage(JacksonUtil.objectToJson(response)));
        }
        log.info("response {}", response);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("handleTransportError", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }
}
