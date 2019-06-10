package com.parking.point.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.common.ResponseCode;
import com.parking.point.common.ServerResponse;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.entity.Viechle;
import com.parking.point.mapper.ParkingPointMapper;
import com.parking.point.service.IViechleService;
import com.parking.point.vo.PageResponse;
import com.parking.point.vo.ViechleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 购买车位的用户表 前端控制器
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@RestController
@RequestMapping("/viechle")
@Slf4j
public class ViechleController {

    @Autowired
    private IViechleService viechleService;
    @Autowired(required = false)
    private ParkingPointMapper parkingPointMapper;

    /**
     * 返回所有购买业主的信息  并且分页
     *
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    public PageResponse list(Integer page, Integer limit) {
        Page<Viechle> parkingPointPage = new Page<>();
        parkingPointPage.setCurrent(page);
        parkingPointPage.setSize(limit);
        IPage<Viechle> list = viechleService.listViechle(parkingPointPage);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setCode(String.valueOf(ResponseCode.SUCCESS.getCode()));
        pageResponse.setMsg("");
        pageResponse.setCount((int) list.getTotal());
        pageResponse.setData(list.getRecords());
        return pageResponse;
    }

    @PostMapping("/login")
    public ServerResponse login(@RequestBody Viechle v){
        try {
            //更新二次共享时间信息到对应车位信息
            QueryWrapper<Viechle> wrapper = new QueryWrapper<>();
            wrapper.eq("viechle_number", v.getViechleNumber()).eq("password",v.getPassword());
            Viechle result = viechleService.getOne(wrapper);
            if(ObjectUtils.isEmpty(result)){
                return ServerResponse.createByErrorMessage("登录失败，请检查用户名密码是否正确！");
            }else{
                return ServerResponse.createBySuccess(result);
            }
        } catch (Exception e) {
            log.info("登录失败", e);
            return ServerResponse.createByErrorMessage("登录失败，请检查车牌号密码是否正确！");
        }
    }
    @PostMapping("/updatePassword")
    public ServerResponse updatePassword(@RequestBody Viechle v){
        if(StringUtils.isEmpty(v.getViechleNumber())){
            return ServerResponse.createByErrorMessage("车牌号不能为空！");
        }
        try{
            UpdateWrapper<Viechle> wrapper = new UpdateWrapper<>();
            wrapper.eq("viechle_number", v.getViechleNumber());
            viechleService.update(v, wrapper);
            return ServerResponse.createBySuccess();
        }catch (Exception e){
            log.info("修改密码失败！",e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }


    }

    /**
     * 添加购买信息
     *
     * @param viechleVo
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    @Transactional
    public ServerResponse<Object> add(ViechleVo viechleVo) {
        try {
            //检查车位是否已经被购买
            QueryWrapper<Viechle> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parking_position_code", viechleVo.getParkingPositionCode());
            Viechle viechleServiceOne = viechleService.getOne(queryWrapper);
            if (!ObjectUtils.isEmpty(viechleServiceOne)) {
                return ServerResponse.createByErrorMessage("该车位已经被购买，请选择其他车位");
            }
            //保存购买信息
            Viechle viechle = new Viechle();
            BeanUtils.copyProperties(viechleVo, viechle);
            viechle.setEffectedTime(new Date());
            viechle.setExpiredTime(DateUtils.addHours(new Date(), viechleVo.getTotalDay().intValue()*24));
            //默认密码1234
            viechle.setPassword("1234");
            viechleService.save(viechle);
            //更新车位的共享时间等信息
            UpdateWrapper<ParkingPoint> wrapper = new UpdateWrapper<>();
            wrapper.eq("code", viechleVo.getParkingPositionCode());
            ParkingPoint parkingPoint = new ParkingPoint();
            parkingPoint.setSharing("1");
            parkingPoint.setIsUsed("1");
            parkingPoint.setSharingStartTime(viechle.getEffectedTime());
            parkingPoint.setSharingCountDay(viechleVo.getTotalDay());
            parkingPoint.setSharingEndTime(viechle.getExpiredTime());
            parkingPointMapper.update(parkingPoint, wrapper);
            return ServerResponse.createBySuccess();
        } catch (BeansException e) {
            log.info("添加购买信息失败", e);
            return ServerResponse.createByErrorMessage("添加购买信息失败，请检查后重新操作");
        }
    }
    @GetMapping("/getViechleByNo")
    public ServerResponse<Viechle> getViechleByNo(String viechleNumber) throws ParseException {
        Viechle v = viechleService.getViechleByNo(viechleNumber);
        if(ObjectUtils.isEmpty(v)){
            return ServerResponse.createByErrorMessage("暂无数据");
        }else{
            return ServerResponse.createBySuccess(v);
        }
    }
    /**
     * 再次购买车位
     *
     * @param viechleVo
     * @return
     */
    @ResponseBody
    @RequestMapping("buyAgain")
    @Transactional
    public ServerResponse<Object> buyAgain(ViechleVo viechleVo) {
        //判断车位是否到期
        try{
           viechleService.buyAgain(viechleVo);
            ServerResponse<Object> response = ServerResponse.createBySuccess();
            return response;
        }catch(Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }

    }

    /**
     * 共享车位
     *
     * @param viechleVo
     * @return
     */
    @ResponseBody
    @RequestMapping("share")
    public ServerResponse<Object> share(@RequestBody ViechleVo viechleVo) {

        ServerResponse<Object> response = null;
        try {
            viechleService.share(viechleVo);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            log.info("共享失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

}
