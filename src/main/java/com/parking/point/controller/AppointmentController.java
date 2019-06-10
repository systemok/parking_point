package com.parking.point.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.common.ResponseCode;
import com.parking.point.common.ServerResponse;
import com.parking.point.entity.Appointment;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.service.IAppointmentService;
import com.parking.point.vo.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private IAppointmentService appointmentService;
    @PostMapping("/add")
    public ServerResponse<Appointment> add(@RequestBody Appointment appoint){
        try{
            appointmentService.add(appoint);
            return ServerResponse.createBySuccess();
        }catch(Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @PostMapping("/del")
    public ServerResponse<Appointment> add(String id){
        try{
            appointmentService.removeById(id);
            return ServerResponse.createBySuccess();
        }catch(Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 获取所有车位信息 并且分页处理
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageResponse list(Integer page, Integer limit) {
        Page<Appointment> appointmentPage = new Page<>();
        appointmentPage.setCurrent(page);
        appointmentPage.setSize(limit);
        IPage<Appointment> list = appointmentService.listAppointment(appointmentPage);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setCode(String.valueOf(ResponseCode.SUCCESS.getCode()));
        pageResponse.setMsg("");
        pageResponse.setCount((int) list.getTotal());
        pageResponse.setData(list.getRecords());
        return pageResponse;
    }
}
