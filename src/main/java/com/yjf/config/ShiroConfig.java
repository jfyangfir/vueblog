package com.yjf.config;


import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ShiroConfig {

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDA0) {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDao
        sessionManager.setSessionDAO(redisSessionDA0);
        return sessionManager;
    }

    @Bean
    public SessionsSecurityManager securityHanager(List<Realm> realms,
                                                                   SessionManager sessionManager,
                                                                   RedisCacheManager rediscacheManager) {


        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realms);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        //inject redisCacheManager
        securityManager.setCacheManager(rediscacheManager);

        return securityManager;

    }
}
