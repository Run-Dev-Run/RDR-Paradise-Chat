package com.rdr.paradisechat.common.handler;

import com.rdr.paradisechat.common.properties.SocketProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final SocketProperties socketProperties;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String REGEX = " : ";
    private static final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished()");
        sendMessage(session, socketProperties.getConnectionMessage());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
        throws Exception {
        log.info("handleMessage()");
        sendMessage(session, message.getPayload().toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
        throws Exception {
        log.info("afterConnectionClosed()");
        sendMessage(session, socketProperties.getConnectionClosedMessage());
        sessions.remove(session);
    }

    private void sendMessage(WebSocketSession session, String message) throws Exception {
        String nickName = searchNickName(session);
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(makeMessage(nickName, message));
        }
    }

    public String searchNickName(WebSocketSession session) {
        Map<String, Object> map = session.getAttributes();
        return (String) map.get(socketProperties.getUserNickname());
    }

    private TextMessage makeMessage(String nickName, String message) {
        return new TextMessage(nickName + REGEX + message);
    }
}