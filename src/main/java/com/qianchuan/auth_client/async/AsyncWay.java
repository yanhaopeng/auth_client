package com.qianchuan.auth_client.async;

import com.qianchuan.auth_client.feign.customer.CustomerFeign;
import com.qianchuan.commons.interfac.feigin.customer.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncWay {
    @Autowired
    private CustomerFeign customerFeign;

    /**
     *异步发送短信
     * @param sms
     */
    @Async
    public void sendSms(Sms sms){
        customerFeign.sendSmsNews(sms);
    }
}
