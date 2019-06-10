package com.parking.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.ParkingPoint;
import com.parking.point.entity.Viechle;
import com.parking.point.mapper.ViechleMapper;
import com.parking.point.service.IParkingPointService;
import com.parking.point.service.IViechleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parking.point.vo.ViechleVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 购买车位的用户表 服务实现类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Service
public class ViechleServiceImpl extends ServiceImpl<ViechleMapper, Viechle> implements IViechleService {

    @Autowired
    private IParkingPointService parkingPointService;
    @Override
    public Viechle getViechleByNo(String viechleNumber) {
        if(viechleNumber.isEmpty()){
            return null;
        }
        return baseMapper.getViechleByNo(viechleNumber);
    }

    @Override
    public void clean() {
        baseMapper.cleanViechle();
        baseMapper.cleanViechle1();
    }

    @Override
    public void buyAgain(ViechleVo viechleVo) throws Exception {
        //根据车位号找到买主
        QueryWrapper<Viechle> wrapper = new QueryWrapper<>();
        wrapper.eq("parking_position_code",viechleVo.getParkingPositionCode());
        wrapper.eq("viechle_Number",viechleVo.getViechleNumber());
        wrapper.eq("is_valid","1");
        Viechle viechle = this.getOne(wrapper);
        double totalDay = viechleVo.getTotalDay().doubleValue();
        if(ObjectUtils.isEmpty(viechle)){
            //判断车位是否被别人购买
            QueryWrapper<ParkingPoint> query = new QueryWrapper<ParkingPoint>();
            query.eq("code",viechleVo.getParkingPositionCode());
            query.eq("sharing","0");
            ParkingPoint point = parkingPointService.getOne(query);
            if(ObjectUtils.isEmpty(point)){
                throw new Exception("您的车位已经被占用，请重新购买！");
            }else{
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                viechle.setEffectedTime(sdf.parse(sdf.format(now)));
                viechle.setExpiredTime(DateUtils.addDays(viechle.getEffectedTime(),(int) totalDay));
            }
        }else{
            viechle.setExpiredTime(DateUtils.addDays(viechle.getExpiredTime(),(int)totalDay));
            viechle.setIsValid("1");
            UpdateWrapper<Viechle> update = new UpdateWrapper<Viechle>();
            update.eq("id",viechle.getId());
            if(!this.update(viechle,wrapper)){
                throw new Exception("再次购买失败，请稍后重试！");
            }
        }
    }

    @Override
    public IPage<Viechle> listViechle(Page<Viechle> parkingPointPage) {
        return baseMapper.listViechle(parkingPointPage);
    }

    /**
     * 二次共享
     * @param viechleVo
     */
    @Override
    public void share(ViechleVo viechleVo) throws Exception {
        //找到第一次购买的记录
         QueryWrapper<Viechle> wrapper = new QueryWrapper<>();
         wrapper.eq("parking_position_code",viechleVo.getParkingPositionCode());
         wrapper.eq("viechle_number",viechleVo.getViechleNumber());
         wrapper.eq("is_valid","1");
         Viechle viechle = this.getOne(wrapper);
         if(ObjectUtils.isEmpty(viechle)){
             throw new Exception("您的车位已经失效，请重新购买！");
         }
         if(viechleVo.getEffectiveTime().compareTo(viechleVo.getExpiredTime()) >= 0){
             throw new Exception("共享开始时间不能大于共享结束时间！");
         }
        //判断第一次购买时间是否大于共享时间
        if (viechle.getExpiredTime().compareTo(viechleVo.getEffectiveTime()) == -1 ||
                viechle.getExpiredTime().compareTo(viechleVo.getExpiredTime())== -1) {
            throw new Exception("共享时间超过原购买时间,请减少共享时间");
        }
        //更新购买信息表的状态
        Double totalDay = (double)(viechleVo.getExpiredTime().getTime()-viechleVo.getEffectiveTime().getTime())/1000/60/60/24;
        UpdateWrapper<Viechle> viechleUpdateWrapper = new UpdateWrapper<>();
        viechleUpdateWrapper.eq("parking_position_code",viechleVo.getParkingPositionCode());
        viechleUpdateWrapper.eq("viechle_number",viechleVo.getViechleNumber());
        Viechle v = new Viechle();
        v.setStatus("1");
        v.setSharingStartTime(viechleVo.getEffectiveTime());
        v.setSharingEndTime(viechleVo.getExpiredTime());
        v.setSharingTotalDay(totalDay);
        this.update(v,viechleUpdateWrapper);
        //更新二次共享时间信息到对应车位信息
        UpdateWrapper<ParkingPoint> update1 = new UpdateWrapper<>();
        update1.eq("code", viechleVo.getParkingPositionCode());
        ParkingPoint parkingPoint = new ParkingPoint();
        parkingPoint.setSharingSecond("1");
        parkingPoint.setIsUsed("0");
        parkingPoint.setSharingSecondStartTime(viechleVo.getEffectiveTime());
        parkingPoint.setSharingSecondCountDay(totalDay);
        parkingPoint.setSharingSecondEndTime(viechleVo.getExpiredTime());
        parkingPointService.update(parkingPoint, update1);
    }
}
