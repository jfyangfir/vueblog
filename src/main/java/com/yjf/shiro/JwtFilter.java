package com.yjf.shiro;

import cn.hutool.json.JSONUtil;
import com.yjf.sys.common.Result;
import com.yjf.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isBlank(jwt)){
            return null;
        }
        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isBlank(jwt)){
            return true;
        }else {

            //校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if(claim==null||jwtUtils.isTokenExpired(claim.getExpiration())){
                throw new ExpiredCredentialsException("token已经失效，请重新登录");
            }

            //执行登录
            return executeLogin(servletRequest,servletResponse);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        Throwable throwable = e.getCause()==null?e:e.getCause();

        Result result = Result.fail(throwable.getMessage());

        String json= JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        }catch (Exception e1){}

        return  false;
    }
}
