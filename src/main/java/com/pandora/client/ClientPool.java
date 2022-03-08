package com.pandora.client;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liuqiang
 * @Description //TODO 钉钉客户端线程池$
 * @date 2021/10/11 14:23
 */
public class ClientPool {

    private ClientPool(){}
    private static class DingClients {
        private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    }

    public static ExecutorService acquire() {
       return DingClients.cachedThreadPool;
    }

}
