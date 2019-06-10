package com.parking.point.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.point.entity.Appointment;
import com.parking.point.mapper.AppointmentMapper;
import com.parking.point.service.IAppointmentService;
import com.parking.point.service.IParkingPointService;
import com.sun.jdi.connect.Connector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService{
   @Autowired
   private IParkingPointService parkingPointService;
    @Override
    public void add(Appointment appoint) throws Exception {
        this.isValid(appoint);
        Calendar calendar = Calendar.getInstance();
        Date time = appoint.getApplyTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.MINUTE,1);
        appoint.setExpiredTime(sdf.parse(sdf.format(calendar.getTime())));
        appoint.setId(UUID.randomUUID().toString());
        parkingPointService.updateStatus(appoint.getParkingPointCode(),1);
        baseMapper.insert(appoint);
    }

    @Override
    public void clean() {
        baseMapper.cleanAppointment();
    }

    @Override
    public IPage<Appointment> listAppointment(Page<Appointment> appointmentPage) {
        return baseMapper.listAppointment(appointmentPage);
    }

    private void isValid(Appointment appointment) throws Exception {
        if(ObjectUtils.isEmpty(appointment)){
            throw new Exception("参数不能为空！");
        }
        if(!StringUtils.hasText(appointment.getParkingPointCode())){
            throw new Exception("车位号不能为空！");
        }
        if(!StringUtils.hasText(appointment.getViechleNumber())){
           throw new Exception("车牌号不能为空");
        }
        if(ObjectUtils.isEmpty(appointment.getApplyTime())){
            throw new Exception("预约时间不能为空！");
        }
    }

}
