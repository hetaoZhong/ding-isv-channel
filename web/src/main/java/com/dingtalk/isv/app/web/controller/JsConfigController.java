package com.dingtalk.isv.app.web.controller;

import com.dingtalk.isv.access.api.model.corp.CorpAppVO;
import com.dingtalk.isv.access.api.model.corp.CorpChannelJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.CorpJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.callback.CorpChannelAppVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptException;
import com.dingtalk.oapi.lib.aes.DingTalkJsApiSingnature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lifeng.zlf on 2016/2/24.
 */
@Controller
public class JsConfigController {

    private static final Logger mainLogger = LoggerFactory.getLogger(JsConfigController.class);
    private static final Logger bizLogger = LoggerFactory.getLogger("JSAPI_LOGGER");

    private Long microappAppId = 1949L;                      //该应用原声的appid
    private final String suiteKey = "suitexdhgv7mn5ufoi9ui"; //该应用所属的套件suitekey
    @Resource
    private CorpManageService corpManageService;
    @Resource
    private HttpResult httpResult;

    /**
     * 测试微应用鉴权
     * @param url
     * @param corpId
     * @return
     */
    @RequestMapping("/get_js_config")
    @ResponseBody
    public Map<String, Object>  getJSConfig(@RequestParam(value = "url", required = false) String url,
                              @RequestParam(value = "corpId", required = false) String corpId

    ) {
        try{
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("url", url),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("appId", microappAppId)
            ));
            url = check(url,corpId,suiteKey,microappAppId);
            ServiceResult<CorpJSAPITicketVO> jsapiTicketSr = corpManageService.getCorpJSAPITicket(suiteKey, corpId);
            ServiceResult<CorpAppVO> corpAppVOSr = corpManageService.getCorpApp(corpId, microappAppId);
            String nonce = com.dingtalk.oapi.lib.aes.Utils.getRandomStr(8);
            Long timeStamp = System.currentTimeMillis();
            String sign = DingTalkJsApiSingnature.getJsApiSingnature(url, nonce, timeStamp, jsapiTicketSr.getResult().getCorpJSAPITicket());
            Map<String,Object> jsapiConfig = new HashMap<String, Object>();
            jsapiConfig.put("signature",sign);
            jsapiConfig.put("nonce",nonce);
            jsapiConfig.put("timeStamp",timeStamp);
            jsapiConfig.put("agentId",corpAppVOSr.getResult().getAgentId());
            jsapiConfig.put("corpId",corpId);
            return httpResult.getSuccess(jsapiConfig);
        }catch (Exception e){
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("url", url),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }


    /**
     * 测试服务窗jsapi鉴权
     * @param url
     * @param corpId
     * @param suiteKey
     * @param appId
     * @return

    @RequestMapping("/get_channel_js_config")
    @ResponseBody
    public Map<String, Object>  getChannelJSConfig(@RequestParam(value = "url", required = false) String url,
                                            @RequestParam(value = "corpId", required = false) String corpId,
                                            @RequestParam(value = "suiteKey", required = false) String suiteKey,
                                            @RequestParam(value = "appId", required = false) Long appId

    ) {
        try{
            if(null==suiteKey){
                suiteKey = this.suiteKey;
            }
            if(null==appId){
                appId = this.channelAppAppId;
            }
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("url", url),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("appId", appId)
            ));
            url = check(url,corpId,suiteKey,appId);
            ServiceResult<CorpChannelJSAPITicketVO> jsapiTicketSr = corpManageService.getCorpChannelJSAPITicket(suiteKey, corpId);
            ServiceResult<CorpChannelAppVO> corpAppVOSr = corpManageService.getCorpChannelApp(corpId, appId);
            String nonce = com.dingtalk.oapi.lib.aes.Utils.getRandomStr(8);
            Long timeStamp = System.currentTimeMillis();
            String sign = DingTalkJsApiSingnature.getJsApiSingnature(url, nonce, timeStamp, jsapiTicketSr.getResult().getCorpChannelJSAPITicket());
            Map<String,Object> jsapiConfig = new HashMap<String, Object>();
            jsapiConfig.put("signature",sign);
            jsapiConfig.put("nonce",nonce);
            jsapiConfig.put("timeStamp",timeStamp);
            jsapiConfig.put("agentId",corpAppVOSr.getResult().getAgentId());
            jsapiConfig.put("corpId",corpId);
            return httpResult.getSuccess(jsapiConfig);
        }catch (Exception e){
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("url", url),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
     */


    private String check(String url,String corpId,String suiteKey,Long appId) throws Exception{//TODO 妈蛋的就然没有定义serviceexception
        try {
            url = URLDecoder.decode(url,"UTF-8");
            URL urler = new URL(url);
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(urler.getProtocol());
            urlBuffer.append(":");
            if (urler.getAuthority() != null && urler.getAuthority().length() > 0) {
                urlBuffer.append("//");
                urlBuffer.append(urler.getAuthority());
            }
            if (urler.getPath() != null) {
                urlBuffer.append(urler.getPath());
            }
            if (urler.getQuery() != null) {
                urlBuffer.append('?');
                urlBuffer.append(URLDecoder.decode(urler.getQuery(), "utf-8"));
            }
            url = urlBuffer.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("url非法");
        }
        return url;
    }
}
