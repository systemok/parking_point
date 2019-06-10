package com.parking.point.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.common.ResponseCode;
import com.parking.point.common.ServerResponse;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.entity.Template;
import com.parking.point.entity.Viechle;
import com.parking.point.mapper.ViechleMapper;
import com.parking.point.service.IParkingPointService;
import com.parking.point.service.ITemplateService;
import com.parking.point.vo.PageResponse;
import com.parking.point.vo.TemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 临时用户表 前端控制器
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Controller
@RequestMapping("/template")
@Slf4j
public class TemplateController {

    @Autowired(required = false)
    private ITemplateService templateService;
    @Autowired(required = false)
    private ViechleMapper viechleMapper;
    @Autowired
    private IParkingPointService parkingPointService;

    /**
     * 获取所有临时车进出信息  并且分页处理
     *
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageResponse list(Integer page, Integer limit) {
        Page<Template> parkingPointPage = new Page<>();
        parkingPointPage.setCurrent(page);
        parkingPointPage.setSize(limit);
        QueryWrapper<Template> wrapper = new QueryWrapper<Template>();
        wrapper.isNull("end_time");
        IPage<Template> list = templateService.page(parkingPointPage,wrapper);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setCode(String.valueOf(ResponseCode.SUCCESS.getCode()));
        pageResponse.setMsg("");
        pageResponse.setCount((int) list.getTotal());
        pageResponse.setData(list.getRecords());
        return pageResponse;
    }

    /**
     * 临时车进场
     *
     * @param templateVo
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public ServerResponse<Object> add(TemplateVo templateVo) {
        try {
            //判断是否购买的车主的车牌
            QueryWrapper<Viechle> wrapper = new QueryWrapper<>();
            wrapper.eq("viechle_number", templateVo.getViechleNumber());
            //wrapper.eq("parking_position_code",templateVo.getParkingPositionCode());
            Viechle viechle = viechleMapper.selectOne(wrapper);
            if (!ObjectUtils.isEmpty(viechle) && !ObjectUtils.isEmpty(viechle.getEffectedTime())) {
                return ServerResponse.createByErrorMessage("是长期车位的车主，直接进场!");
            }
            //保存驶入车辆信息
            Template template = new Template();
            BeanUtils.copyProperties(templateVo, template);
            template.setStartTime(new Date());
            templateService.save(template);
            //修改车位状态为占用
            UpdateWrapper<ParkingPoint> update = new UpdateWrapper<>();
            update.eq("code",templateVo.getParkingPositionCode());
            ParkingPoint p = new ParkingPoint();
            p.setIsUsed("1");
            parkingPointService.update(p,update);
            return ServerResponse.createBySuccess();
        } catch (BeansException e) {
            log.info("添加驶入车辆失败", e);
            return ServerResponse.createByErrorMessage("添加驶入车辆失败,请检查重新添加");
        }
    }

    /**
     * 车辆驶出计费
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("vehicleLeaving")
    public ServerResponse<Map<String, Object>> vehicleLeaving(Integer id) {
        try {
            Map<String, Object> map = templateService.vehicleLeaving(id);
            //更新车位状态
            UpdateWrapper<ParkingPoint> update = new UpdateWrapper<>();
            update.eq("code",templateService.getById(id).getParkingPositionCode());
            ParkingPoint p = new ParkingPoint();
            p.setIsUsed("0");
            parkingPointService.update(p,update);
            return ServerResponse.createBySuccess(map);
        } catch (Exception e) {
            log.info("驶出计费失败", e);

            return ServerResponse.createByErrorMessage("驶出后计费失败，请检查后重新操作");
        }

    }
    /**
     * 一天的车辆进出统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("statistic")
    public ServerResponse<Object> statistic() {
        try {
            Map<String,Object> list = templateService.statistic();
            return ServerResponse.createBySuccess(list);
        } catch (Exception e) {
            log.info("驶入驶出统计失败", e);
            return ServerResponse.createByErrorMessage("驶入驶出统计失败，请检查后重新操作");
        }

    }


}
