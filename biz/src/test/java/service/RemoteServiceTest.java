package service;

import base.BaseTestCase;
import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.dingtalk.isv.access.api.model.corp.CorpJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.model.suite.CorpSuiteAuthVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.suite.CorpSuiteAuthService;
import com.dingtalk.isv.common.model.ServiceResult;
import org.junit.Test;

import javax.annotation.Resource;
import java.net.MalformedURLException;

/**
 * Created by mint on 16-1-22.
 */
public class RemoteServiceTest extends BaseTestCase {


    @Resource
    private CorpManageService corpManageService;
    @Resource
    private CorpSuiteAuthService corpSuiteAuthService;
    @Test
    public void test_getCorpToken() {
        String corpId="ding4ed6d279061db5e7";
        String suiteKey="suite4rkgtvvhr1neumx2";
        ServiceResult<CorpTokenVO> sr = corpManageService.getCorpToken(suiteKey,corpId);
        System.err.println(JSON.toJSONString(sr));
    }


    @Test
    public void test_getCorpSuiteAuth() {
        String corpId="ding4ed6d279061db5e7";
        String suiteKey="suiteytzpzchcpug3xpsm";
        ServiceResult<CorpSuiteAuthVO> sr = corpSuiteAuthService.getCorpSuiteAuth(corpId,suiteKey);
    }

















}
