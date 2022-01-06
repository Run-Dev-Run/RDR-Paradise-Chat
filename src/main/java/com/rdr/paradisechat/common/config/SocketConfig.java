package com.rdr.paradisechat.common.config;

import com.rdr.paradisechat.common.handler.SocketHandler;
import com.rdr.paradisechat.common.properties.SocketProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableConfigurationProperties(SocketProperties.class)
@EnableWebSocket
@RequiredArgsConstructor
public class SocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;
    private final SocketProperties socketProperties;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, socketProperties.getUrl())
            .setAllowedOrigins(socketProperties.getOrigin());
    }
}