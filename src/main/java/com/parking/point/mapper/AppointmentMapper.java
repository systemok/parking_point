package com.parking.point.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.Appointment;

public interface AppointmentMapper extends BaseMapper<Appointment> {
    int deleteByPrimaryKey(String id);

    int insert(Appointment record);

    int insertSelective(Appointment record);

    Appointment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);

    void cleanAppointment();

    IPage<Appointment> listAppointment(Page<Appointment> appointmentPage);
}