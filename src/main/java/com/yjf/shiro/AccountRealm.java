package com.yjf.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.yjf.sys.entity.MyUser;
import com.yjf.sys.service.MyUserService;
import com.yjf.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MyUserService myUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /*资源授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /*身份验证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) token;

        String userId= jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        MyUser user=myUserService.getById(Long.valueOf(userId));

        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }

        if(user.getStatus()==-1){
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        System.out.println("--------------");

        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
