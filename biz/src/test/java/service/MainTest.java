package service;

import base.BaseTestCase;
import com.caucho.hessian.client.HessianProxyFactory;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.common.model.ServiceResult;
import org.junit.Test;

import javax.annotation.Resource;
import java.net.MalformedURLException;

/**
 * Created by mint on 16-1-22.
 */
public class MainTest {



    public static void main(String[] args) {
        String url = "http://30.26.118.3:8080/ding-isv-access/r/CorpManageService";
        HessianProxyFactory factory = new HessianProxyFactory();
        try {
            CorpManageService testService = (CorpManageService) factory.create(CorpManageService.class, url);
            System.out.println(testService.getCorpApp("ding4ed6d279061db5e7",256L));
            //System.err.println(corpManageService.deleteCorpToken(suiteKey,corpId));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
