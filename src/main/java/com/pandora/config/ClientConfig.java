package com.pandora.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuqiang
 * @Description //TODO 钉钉API配置$
 * @date 2021/10/11 14:09
 */
@ConfigurationProperties(prefix = "dingding")
public class ClientConfig {
private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
