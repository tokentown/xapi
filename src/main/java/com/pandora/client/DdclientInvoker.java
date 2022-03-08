package com.pandora.client;

import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserCreateRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserCreateResponse;
import com.pandora.config.ClientConfig;
import com.pandora.exception.DdApiException;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author liuqiang
 * @Description //TODO 钉钉客户端调用$
 * @date 2021/10/12 9:50
 */
public class DdclientInvoker {
    private ClientConfig config;
    private static Logger logger = LoggerFactory.getLogger(DdclientInvoker.class);
    public DdclientInvoker(ClientConfig config) {
        this.config = config;
    }

    public String auth(String appKey,String appSecret)  {
        String accessToken = "";
        try {
            logger.debug("access...");
            if (StringUtils.isEmpty(appKey)) {
                throw new DdApiException("appKey为空");
            }
            if (StringUtils.isEmpty(appSecret)) {
                throw new DdApiException("appSecret为空");
            }
            DingTalkClient client = getClient("gettoken");
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(appKey);
            req.setAppsecret(appSecret);
            req.setHttpMethod("GET");
            OapiGettokenResponse rsp = client.execute(req);
            if (rsp.isSuccess()) {
                accessToken = rsp.getAccessToken();
            } else {
                throw new DdApiException(rsp.getErrmsg()+":"+rsp.getErrorCode());
            }
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (ApiException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        }
        return accessToken;
    }
    public void addUser (String accessToken,String name,String orgIds,String telNo) {
        logger.debug("addUser...");
        if (StringUtils.isEmpty(accessToken)) {
            throw new DdApiException("accessToken为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw new DdApiException("用户姓名为空");
        }
        if (StringUtils.isEmpty(orgIds)) {
            throw new DdApiException("用户所属组织必须分配");
        }
        if (StringUtils.isEmpty(telNo)) {
            throw new DdApiException("用户手机号为空");
        }
        try {
        DingTalkClient client = getClient("topapi/v2/user/create");
        OapiV2UserCreateRequest req = new OapiV2UserCreateRequest();
        req.setName(name);
        req.setDeptIdList(orgIds);
        req.setMobile(telNo);
        OapiV2UserCreateResponse rsp = client.execute(req, accessToken);
            if (!rsp.isSuccess()) {
                throw new DdApiException(rsp.getErrmsg()+":"+rsp.getErrorCode());
            }
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (ApiException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        }
    }
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getOrg(String accessToken, Long orgId) {
        logger.debug("addUser...");
        if (StringUtils.isEmpty(accessToken)) {
            throw new DdApiException("accessToken为空");
        }
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> orgs = null;
        try {
            DingTalkClient client = getClient("topapi/v2/user/create");
            OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
            if (orgId != null || orgId != 0) {
                req.setDeptId(orgId);
            }
            OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
            if (!rsp.isSuccess()) {
                throw new DdApiException(rsp.getErrmsg()+":"+rsp.getErrorCode());
            } else {
                orgs = rsp.getResult();
            }
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (ApiException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        }
        return orgs;
    }
    public void pushNormalMsg(String accessToken,Long agentId, String userIds,String orgIds, String content) {//有巨大改造空间，可以给你图片的URL
        logger.debug("pushNormalMsg...");
        if (StringUtils.isEmpty(accessToken)) {
            throw new DdApiException("accessToken为空");
        }
        if (StringUtils.isEmpty(agentId)) {
            throw new DdApiException("小程序agentId为空");
        }
        if (StringUtils.isEmpty(userIds) && StringUtils.isEmpty(orgIds)) {
            throw new DdApiException("接受消息方机构或者用户必须填写其中一个");
        }
        try {
            DingTalkClient client = getClient("topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(agentId);
            if (!StringUtils.isEmpty(userIds)) {
                req.setUseridList(userIds);
            }
            if (!StringUtils.isEmpty(orgIds)) {
                req.setDeptIdList(orgIds);
            }
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text con = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            con.setContent(content);
            msg.setText(con);
            req.setMsg(msg);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, accessToken);
            if (!rsp.isSuccess()) {
                throw new DdApiException(rsp.getErrmsg()+":"+rsp.getErrorCode());
            }
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (ApiException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        }
    }
    public void pushLinkMsg(String accessToken,Long agentId, String userIds,String orgIds, String content,String title,String href) {//有巨大改造空间，可以给你图片的URL
        logger.debug("pushLinkMsg...");
        if (StringUtils.isEmpty(accessToken)) {
            throw new DdApiException("accessToken为空");
        }
        if (StringUtils.isEmpty(agentId)) {
            throw new DdApiException("小程序agentId为空");
        }
        if (StringUtils.isEmpty(userIds) && StringUtils.isEmpty(orgIds)) {
            throw new DdApiException("接受消息方机构或者用户必须填写其中一个");
        }
        if (StringUtils.isEmpty(title)) {
            throw new DdApiException("消息通知题目不能为空");
        }
        if (StringUtils.isEmpty(href)) {
            throw new DdApiException("消息超链接不能为空");
        }
        try {
            DingTalkClient client = getClient("topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(agentId);
            if (!StringUtils.isEmpty(userIds)) {
                req.setUseridList(userIds);
            }
            if (!StringUtils.isEmpty(orgIds)) {
                req.setDeptIdList(orgIds);
            }
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("link");
            OapiMessageCorpconversationAsyncsendV2Request.Link con = new OapiMessageCorpconversationAsyncsendV2Request.Link();
            con.setPicUrl("@lADOADmaWMzazQKA");
            con.setMessageUrl(href);
            con.setText(content);
            con.setTitle(title);
            msg.setLink(con);
            req.setMsg(msg);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, accessToken);
            if (!rsp.isSuccess()) {
                throw new DdApiException(rsp.getErrmsg()+":"+rsp.getErrorCode());
            }
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        } catch (ApiException ex) {
            logger.error(ex.getMessage());
            throw new DdApiException(ex.getMessage());
        }
    }
    private DingTalkClient getClient(String action) throws ExecutionException, InterruptedException {
        DingTalkClient client = ClientPool.acquire().submit(new DDClient(config.getServerUrl()+"/"+action)).get();
        return client;
    }
}
