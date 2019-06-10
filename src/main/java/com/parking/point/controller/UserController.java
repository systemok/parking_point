package com.parking.point.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.parking.point.common.ServerResponse;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.entity.User;
import com.parking.point.entity.Viechle;
import com.parking.point.service.ITemplateService;
import com.parking.point.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired(required = false)
    private IUserService userService;

    @PostMapping("/login")
    public ServerResponse<User> login(@RequestBody User user){
        try {
            //更新二次共享时间信息到对应车位信息
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getUserId()).eq("password",user.getPassword());
            User result = userService.getOne(wrapper);
            if(ObjectUtils.isEmpty(result)){
                return ServerResponse.createByErrorMessage("登录失败，请检查用户名密码是否正确！");
            }else{
                return ServerResponse.createBySuccess(result);
            }
        } catch (Exception e) {
            log.info("登录失败", e);
            return ServerResponse.createByErrorMessage("共享失败，请检查后重新操作");
        }
    }
    @GetMapping("/balance")
    public ServerResponse<User> balance(){
        try {
            //更新二次共享时间信息到对应车位信息
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            User result = userService.getOne(wrapper);
            if(ObjectUtils.isEmpty(result)){
                return ServerResponse.createByErrorMessage("获取账户余额失败！");
            }else{
                return ServerResponse.createBySuccess(result);
            }
        } catch (Exception e) {
            log.info("获取账户余额失败", e);
            return ServerResponse.createByErrorMessage("获取账户余额失败");
        }
    }

}
