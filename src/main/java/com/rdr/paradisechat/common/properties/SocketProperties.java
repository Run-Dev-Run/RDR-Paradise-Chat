package com.rdr.paradisechat.common.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "socket")
public class SocketProperties {

    private String url;
    private String origin;
    private String connectionMessage;
    private String connectionClosedMessage;
    private String userNickname;
}