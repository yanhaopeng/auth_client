package com.qianchuan.auth_client.feign.customer;

import com.qianchuan.commons.interfac.feigin.customer.Sms;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/25 13:24
 */
@FeignClient(value = "customer")
public interface CustomerFeign {
    /**
     * 发送短信
     * @param sms
     * @return
     */
    @RequestMapping(value = "/sms/sendSmsNews", method = RequestMethod.POST)
    Sms sendSmsNews(@RequestBody(required = false) Sms sms);
}
