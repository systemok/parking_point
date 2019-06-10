package com.parking.point.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.common.ResponseCode;
import com.parking.point.common.ServerResponse;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.service.IParkingPointService;
import com.parking.point.vo.PageResponse;
import com.parking.point.vo.ParkingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 车位表 前端控制器
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Controller
@RequestMapping("/parking-point")
@Slf4j
public class ParkingPointController {

    @Autowired
    private IParkingPointService parkingPointService;

    /**
     * 获取所有车位信息 并且分页处理
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageResponse list(Integer page, Integer limit) {
        Page<ParkingPoint> parkingPointPage = new Page<>();
        parkingPointPage.setCurrent(page);
        parkingPointPage.setSize(limit);
        IPage<ParkingPoint> list = parkingPointService.listParkingPoint(parkingPointPage);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setCode(String.valueOf(ResponseCode.SUCCESS.getCode()));
        pageResponse.setMsg("");
        pageResponse.setCount((int) list.getTotal());
        pageResponse.setData(list.getRecords());
        return pageResponse;
    }
    /**
     * 根据ID查询车位
     * @return
     */
    @ResponseBody
    @RequestMapping("statisticPointBuy")
    public   ServerResponse<Object> statisticPointBuy(){
        try {
           List<Map<String,Object>> result = parkingPointService.statisticPointBuy();
            //List<ParkingPoint> list = parkingPointService.notUsedParkingPoint();
            return  ServerResponse.createBySuccess(result);
        } catch (Exception e) {
            log.info("查询所有车位信息失败",e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }


    /**
     * 根据ID查询车位
     * @return
     */
    @ResponseBody
    @RequestMapping("getParkingPointByPPCode")
    public   ServerResponse<ParkingPoint> getParkingPointById(HttpServletRequest request){
        String code = request.getParameter("code");
        ServerResponse<ParkingPoint> response = null;
        try {
            ParkingPoint parkingPoint = parkingPointService.getParkingPointByPPCode(code);
            //List<ParkingPoint> list = parkingPointService.notUsedParkingPoint();
            return  ServerResponse.createBySuccess(parkingPoint);
        } catch (Exception e) {
            log.info("查询所有车位信息失败",e);
        }
        return response;

    }

    /**
     * 获取所有车位信息
     * @return
     */
    @ResponseBody
    @RequestMapping("allList")
    public   ServerResponse<List<ParkingPoint>> allList(String mean){
        ServerResponse<List<ParkingPoint>> response = null;
        try {
           // List<ParkingPoint> list = parkingPointService.allList();
            List<ParkingPoint> list = parkingPointService.list(mean);
            return  ServerResponse.createBySuccess(list);
        } catch (Exception e) {
            log.info("查询所有车位信息失败",e);
        }
        return response;

    }

    /**
     *  根据id删除单条车位信息
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("del")
    public ServerResponse<Object> delete(String code) {
        try {
            parkingPointService.removeByCode(code);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            log.info("删除车位信息失败，id:{}",code,e);
            return ServerResponse.createByErrorMessage("删除车位信息失败");
        }

    }

    /**
     *   添加一个车位信息
     * @param parkingVo
     * @return
     */

    @ResponseBody
    @RequestMapping("add")
    public ServerResponse<Object> add(@RequestBody ParkingVo parkingVo) {
        try {
            ParkingPoint parkingPoint = new ParkingPoint();
            BeanUtils.copyProperties(parkingVo, parkingPoint);
            QueryWrapper<ParkingPoint> wrapper = new QueryWrapper<>();
            //根据车位号查询 判断重复
            wrapper.eq("code",parkingPoint.getCode());
            ParkingPoint  parkingPointServiceOne= parkingPointService.getOne(wrapper);
            if(!ObjectUtils.isEmpty(parkingPointServiceOne)){
                return ServerResponse.createByErrorMessage("车位号已经存在，请重新输入");
            }
            parkingPoint.setId(UUID.randomUUID().toString());
            parkingPoint.setSharing("0");
            parkingPoint.setSharingSecond("0");
            parkingPoint.setIsUsed("0");
            //保存新车位信息
            parkingPointService.save(parkingPoint);
           return  ServerResponse.createBySuccess();
        } catch (BeansException e) {
            log.info("添加车位失败",e);
            return  ServerResponse.createByErrorMessage("添加车位失败");
        }
    }
    /**
     * 修改车位距离数据
     */
    @ResponseBody
    @RequestMapping("edit")
    public ServerResponse<Object> edit(@RequestBody ParkingVo vo){
        try{
            parkingPointService.edit(vo);
            return ServerResponse.createBySuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    /**
     * 修改车位距离数据
     */
    @ResponseBody
    @RequestMapping("check")
    public ServerResponse<Object> check(){
        try{
             parkingPointService.check();
            return ServerResponse.createBySuccess();
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

}
