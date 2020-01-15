package com.example.springbootws.ws.handler;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.springbootws.ws.MessageAuthPayload;
import com.example.springbootws.ws.MessagePayload;
import com.example.springbootws.lib.JacksonUtil;
import com.example.springbootws.ws.anno.Session;
import com.example.springbootws.ws.PathMappingManager;
import com.example.springbootws.ws.session.SessionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 * @author Catfish
 */
@Slf4j
public class MainWebSocketHandler extends TextWebSocketHandler {
    private final PathMappingManager pmMgr;
    private final SessionManager sMgr;

    public MainWebSocketHandler(PathMappingManager pmMgr, SessionManager sMgr) {
        this.pmMgr = pmMgr;
        this.sMgr = sMgr;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New Connection {}", session.getId());
        sMgr.connect(session);
        //TODO add auth timeout
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("New Message {}", session.getId());
        MessagePayload payload = JacksonUtil.jsonToObject(message.getPayload(), MessagePayload.class);
        if (Objects.isNull(payload)) {
            throw new IllegalArgumentException("payload format is unavailable");
        }
        if(Objects.nonNull(payload.getType())) {
            if (PayloadType.HEARTBEAT == payload.getType()) {
                sMgr.heartbeat(session.getId());
                return;
            } else if (PayloadType.AUTH == payload.getType()) {
                MessageAuthPayload authPayload = JacksonUtil.mapToBean(payload.getData(), MessageAuthPayload.class);
                //TODO auth mgr
                return;
            }
        }
        PathMappingManager.Wrapper wrapper = pmMgr.get(payload.getPath());
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
                params[i] = JacksonUtil.mapToBean(payload.getData(), paramClz);
                payloadFlag = true;
            }
            if (ss.size() > 0) {
                if (sessionFlag) {
                    throw new IllegalArgumentException("method " + wrapper.getMethod().getName() + "has duplicated @Session parameters");
                }
                params[i] = sMgr.get(session.getId());
                sessionFlag = true;
            }
        }
        Object response = pmMgr.invoke(payload.getPath(), params);
        if (Objects.nonNull(response)) {
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
