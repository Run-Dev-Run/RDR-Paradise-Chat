package com.rdr.paradisechat.common.handler;

import java.lang.System.LoggerFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final String CONNECTION_MESSAGE = "님이 접속했습니다.";
    private static final String CONNECTION_CLOSED_MESSAGE = "님의 연결이 끊어졌습니다.";
    private static final String NICKNAME_PARAM = "nickName";
    private static final String REGEX = " : ";

    private static final List<WebSocketSession> sessions = new ArrayList<>();

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished()");
        sendMessage(session, CONNECTION_MESSAGE);
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
        sendMessage(session, CONNECTION_CLOSED_MESSAGE);
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
        return (String) map.get(NICKNAME_PARAM);
    }

    private TextMessage makeMessage(String nickName, String message) {
        return new TextMessage(nickName + REGEX + message);
    }
}