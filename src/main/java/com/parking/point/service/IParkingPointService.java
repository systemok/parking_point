package com.parking.point.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.Appointment;
import com.parking.point.entity.ParkingPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parking.point.vo.ParkingVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车位表 服务类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface IParkingPointService extends IService<ParkingPoint> {
    List<ParkingPoint> notUsedParkingPoint();

       //根据ID查询车位信息
    ParkingPoint getParkingPointByPPCode(String code);

    void updateStatus(String parkingPointCode,int status) throws Exception;


    void edit(ParkingVo vo) throws Exception;

    void check();

    IPage<ParkingPoint> listParkingPoint(Page<ParkingPoint> parkingPointPage);
    List<ParkingPoint> list(String mean);

    void removeByCode(String code);

    List<Map<String,Object>> statisticPointBuy();
}
