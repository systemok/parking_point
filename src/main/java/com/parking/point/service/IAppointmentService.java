package com.parking.point.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parking.point.entity.Appointment;

public interface IAppointmentService extends IService<Appointment> {
    void add(Appointment appoint) throws Exception;

    void clean();

    IPage<Appointment> listAppointment(Page<Appointment> appointmentPage);
}
