package com.parking.point.service.impl;

import com.parking.point.entity.User;
import com.parking.point.mapper.UserMapper;
import com.parking.point.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
