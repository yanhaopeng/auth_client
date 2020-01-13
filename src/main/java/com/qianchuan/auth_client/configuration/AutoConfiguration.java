package com.qianchuan.auth_client.configuration;

import com.qianchuan.auth_client.config.ServiceAuthConfig;
import com.qianchuan.auth_client.config.UserAuthConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:40
 */
@Configuration
@ComponentScan({"com.qianchuan.auth_client","com.qianchuan.commons.event"})
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
