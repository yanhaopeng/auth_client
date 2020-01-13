
package com.qianchuan.auth_client.jwt;

import com.qianchuan.auth_client.config.ServiceAuthConfig;
import com.qianchuan.auth_client.feign.ServiceAuthFeign;
import com.qianchuan.auth_client.interceptor.OkHttpTokenInterceptor;
import com.qianchuan.commons.admincommons.exception.auth.ClientTokenException;
import com.qianchuan.commons.admincommons.msg.BaseResponse;
import com.qianchuan.commons.admincommons.msg.ObjectRestResponse;
import com.qianchuan.commons.authcommons.util.jwt.IJWTInfo;
import com.qianchuan.commons.authcommons.util.jwt.JWTHelper;
import com.qianchuan.commons.commons.exception.CustomInternalException;
import com.qianchuan.commons.commons.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:36
 */
@Configuration
@EnableScheduling
public class ServiceAuthUtil{

    private static final Logger logger = LoggerFactory.getLogger(ServiceAuthUtil.class);
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    private List<String> allowedClient;
    private String clientToken;


    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, serviceAuthConfig.getPubKeyByte());
        } catch (ExpiredJwtException ex) {
            throw new CustomInternalException(ErrorCode.ClientTokenExpired,"Client token expired!");
        } catch (SignatureException ex) {
            throw new CustomInternalException(ErrorCode.ClientTokenSignatureError,"Client token signature error!");
        } catch (IllegalArgumentException ex) {
            throw new CustomInternalException(ErrorCode.ClientTokenEmpty,"Client token is null or empty!");
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void refreshAllowedClient() {
        logger.debug("refresh allowedClient.....");
        BaseResponse resp = serviceAuthFeign.getAllowedClient(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 200) {
            ObjectRestResponse<List<String>> allowedClient = (ObjectRestResponse<List<String>>) resp;
            this.allowedClient = allowedClient.getData();
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshClientToken() {
        logger.debug("refresh client token.....");
        BaseResponse resp = serviceAuthFeign.getAccessToken(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 200) {
            ObjectRestResponse<String> clientToken = (ObjectRestResponse<String>) resp;
            this.clientToken = clientToken.getData();
        }
    }


    public String getClientToken() {
        if (this.clientToken == null) {
            this.refreshClientToken();
        }
        return clientToken;
    }

    public List<String> getAllowedClient() {
        if (this.allowedClient == null) {
            this.refreshAllowedClient();
        }
        return allowedClient;
    }
}