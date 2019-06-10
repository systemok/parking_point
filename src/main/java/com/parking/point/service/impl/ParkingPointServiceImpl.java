package com.parking.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.entity.Viechle;
import com.parking.point.mapper.ParkingPointMapper;
import com.parking.point.service.IAppointmentService;
import com.parking.point.service.IParkingPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.point.service.IViechleService;
import com.parking.point.vo.ParkingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Wrapper;
import java.util.*;

/**
 * <p>
 * 车位表 服务实现类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Service(value="parkingPointService")
public class ParkingPointServiceImpl extends ServiceImpl<ParkingPointMapper, ParkingPoint> implements IParkingPointService {

    @Autowired
    private ParkingPointMapper parkingPointMapper;
    @Autowired
    private IViechleService viechleService;
    @Autowired
    private IAppointmentService appointmentService;

    @Override
    public List<ParkingPoint> notUsedParkingPoint(){
        return parkingPointMapper.notUsedParkingPoint();
    }

    //根据ID查询车位信息
    @Override
   public ParkingPoint getParkingPointByPPCode(String code){
        return parkingPointMapper.getParkingPointByPPCode( code);
    }

    @Override
    public void updateStatus(String parkingPointCode,int status) throws Exception {
        int result = baseMapper.updateStatus(parkingPointCode,status);
        if(result <= 0){
            throw new Exception("预约失败！");
        }
    }

    @Override
    public void edit(ParkingVo vo) throws Exception {
        //更新车位距离
        UpdateWrapper<ParkingPoint> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("code",vo.getCode());
        ParkingPoint parkingPoint = new ParkingPoint();
        parkingPoint.setDistance(vo.getDistance());
        if(baseMapper.update(parkingPoint,updateWrapper) <= 0){
            throw new Exception("更新失败！请稍后重试");
        }
    }

    /**
     * 检查清理失效的停车位购买，预约，二次共享时间
     */
    @Override
    public void check() {
        baseMapper.cleanSharing();
        viechleService.clean();
        appointmentService.clean();
        baseMapper.cleanSharingSecond();
        baseMapper.cleanIsUsed1();
        baseMapper.cleanIsUsed();
    }

    @Override
    public IPage<ParkingPoint> listParkingPoint(Page<ParkingPoint> parkingPointPage) {
        return baseMapper.listParkingPoint(parkingPointPage);
    }


    @Override
    public List<ParkingPoint> list(String mean){
        return baseMapper.list(mean);
    }

    @Override
    public void removeByCode(String code) {
        baseMapper.removeByCode(code);
    }

    @Override
    public List<Map<String, Object>> statisticPointBuy() {
        List<Map<String, Object>> result = baseMapper.statisticPointBuy();
        return result;
    }
}
