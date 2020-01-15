package com.example.springbootws.ws.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.springbootws.lib.DateUtil;

import lombok.Data;

/**
 * @author Catfish
 * @version V1.0 2020/1/15 10:12
 * @email catfish_lty@qq.com
 */
public class SessionManager {
    private Map<String, SessionWrapper> m;
    private Map<String, String> loginM;
    private Map<String, String> loginRM;

    public SessionManager() {
        this.m = new HashMap<>();
        this.loginM = new HashMap<>();
        this.loginRM = new HashMap<>();
    }

    public String connect(WebSocketSession session) {
        m.put(session.getId(), new SessionWrapper(session));
        return session.getId();
    }

    public void disconnect(String sessionId) throws IOException {
        if (!m.containsKey(sessionId)) {
            return;
        }
        m.get(sessionId).close();
        m.remove(sessionId);
        if (loginRM.containsKey(sessionId)) {
            loginM.remove(loginRM.get(sessionId));
            loginRM.remove(sessionId);
        }
    }

    public void login(String uid, String sessionId) {
        this.loginM.put(uid, sessionId);
        this.loginRM.put(sessionId, uid);
    }

    public void logout(String uid) throws IOException {
        String sessionId = this.loginM.get(uid);
        if (StringUtils.isBlank(uid)) {
            return;
        }
        this.disconnect(sessionId);
    }

    public void heartbeat(String sessionId) {
        if (!m.containsKey(sessionId)) {
            return;
        }
        m.get(sessionId).setUpdateAt(DateUtil.getNowTimeMills());
    }

    public SessionWrapper get(String sessionId) {
        return m.get(sessionId);
    }

    @Data
    public static class SessionWrapper {
        private WebSocketSession session;
        private String uid;
        private Long connectAt;
        private Long updateAt;

        public SessionWrapper(WebSocketSession session) {
            this.session = session;
            this.connectAt = DateUtil.getNowTimeMills();
            this.updateAt = this.connectAt;
        }

        public boolean isLogin() {
            return StringUtils.isNotBlank(uid);
        }

        public boolean isConnect() {
            return session.isOpen();
        }

        public void close() throws IOException {
            session.close(CloseStatus.SERVER_ERROR);
        }

        public void send(String data) throws IOException {
            session.sendMessage(new TextMessage(data));
        }
    }
}
