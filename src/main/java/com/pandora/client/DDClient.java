package com.pandora.client;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;

import java.util.concurrent.Callable;

/**
 * @author liuqiang
 * @Description //TODO 钉钉客户端$
 * @date 2021/10/11 14:59
 */
public class DDClient implements Callable<DingTalkClient> {


    private String url = null;

    public DDClient(String url) {
        this.url = url;
    }


    @Override
    public DingTalkClient call() {
        return new DefaultDingTalkClient(url);
    }
}
