package com.yjf.config;


import com.yjf.shiro.AccountRealm;
import com.yjf.shiro.JwtFilter;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDA0) {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDao
        sessionManager.setSessionDAO(redisSessionDA0);
        return sessionManager;
    }

    @Bean
    public SessionsSecurityManager securityManager(AccountRealm accountRealm,
                                                   SessionManager sessionManager,
                                                   RedisCacheManager rediscacheManager) {


        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        //inject redisCacheManager
        securityManager.setCacheManager(rediscacheManager);

        return securityManager;

    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterchainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**","authc");
        // 主要通过注解方式校验权限
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterchainDefinition){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);

        Map<String,String > filterMap = shiroFilterchainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

}
