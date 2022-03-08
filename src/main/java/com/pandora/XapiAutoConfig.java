package com.pandora;

import com.pandora.client.ClientPool;
import com.pandora.client.DDClient;
import com.pandora.client.DdclientInvoker;
import com.pandora.config.ClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author liuqiang
 * @Description //TODO 程序入口$
 * @date 2021/10/11 14:07
 */
@Configuration
@EnableConfigurationProperties(ClientConfig.class)
public class XapiAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public DdclientInvoker createDdApi(ClientConfig clientConfig) {
        return new DdclientInvoker(clientConfig);
    }

}
