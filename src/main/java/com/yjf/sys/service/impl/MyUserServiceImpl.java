package com.yjf.sys.service.impl;

import com.yjf.sys.entity.MyUser;
import com.yjf.sys.mapper.MyUserMapper;
import com.yjf.sys.service.MyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Eric
 * @since 2020-11-04
 */
@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, MyUser> implements MyUserService {

}
