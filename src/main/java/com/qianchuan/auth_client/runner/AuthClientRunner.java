package com.qianchuan.auth_client.runner;

import com.qianchuan.auth_client.config.ServiceAuthConfig;
import com.qianchuan.auth_client.config.UserAuthConfig;
import com.qianchuan.auth_client.feign.ServiceAuthFeign;
import com.qianchuan.commons.admincommons.msg.BaseResponse;
import com.qianchuan.commons.admincommons.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.Jedis;

/**
 * 监听完成时触发
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:38
 */
@Configuration
@Slf4j
public class AuthClientRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AuthClientRunner.class);
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;
    @Autowired
    private UserAuthConfig userAuthConfig;
    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    public static void main(String[] args) {

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("初始化加载用户pubKey");


        try {
            refreshUserPubKey();
        }catch(Exception e){
            logger.error("初始化加载用户pubKey失败,1分钟后自动重试!",e);
        }
        logger.info("初始化加载客户pubKey");
        try {
            refreshServicePubKey();
        }catch(Exception e){
            logger.error("初始化加载客户pubKey失败,1分钟后自动重试!",e);
        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshUserPubKey(){
        BaseResponse resp = serviceAuthFeign.getUserPublicKey(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == HttpStatus.OK.value()) {

//            Jedis jedis=new Jedis("47.98.220.94",6379);
//            jedis.auth("123456");
//            String value=jedis.get("auth_client");
//            System.out.println(value);
//            if (!"4W0m9bX0ykQtVzDQiomxAAS".equals(value)){
//                try {
//                    throw new Exception("秘钥过期");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    jedis.close();
//                }
//            }
            ObjectRestResponse<byte[]> userResponse = (ObjectRestResponse<byte[]>) resp;
            this.userAuthConfig.setPubKeyByte(userResponse.getData());
        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshServicePubKey(){
        BaseResponse resp = serviceAuthFeign.getServicePublicKey(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == HttpStatus.OK.value()) {
            ObjectRestResponse<byte[]> userResponse = (ObjectRestResponse<byte[]>) resp;
            this.serviceAuthConfig.setPubKeyByte(userResponse.getData());
        }
    }

}