package com.parking.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.parking.point.entity.Template;
import com.parking.point.entity.User;
import com.parking.point.entity.Viechle;
import com.parking.point.mapper.TemplateMapper;
import com.parking.point.mapper.UserMapper;
import com.parking.point.mapper.ViechleMapper;
import com.parking.point.service.ITemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 临时用户表 服务实现类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements ITemplateService {

    @Autowired(required = false)
    private ViechleMapper viechleMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     *  车辆驶出计费
     *  如果该车位是二次共享车位，则将该车位的费用存储在车位购买者余额中，如果未出售，则将余额更新在管理员的账户余额中
     * @param id
     * @return
     */
    @Transactional
    public Map<String, Object> vehicleLeaving(Integer id) {
        //计算费用  保存到临时用户表
        Template template = this.getById(id);
        long time = calculateParkingDuration(template.getStartTime());
        double totalFees = template.getFees() * time;
        template.setEndTime(new Date());
        template.setTotalDay(Double.valueOf(time));
        template.setMoney(totalFees);
        this.updateById(template);
        //查询业主信息
        QueryWrapper<Viechle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking_position_code", template.getParkingPositionCode());
        queryWrapper.eq("is_valid",'1');
        Viechle viechle1 = viechleMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(viechle1)){
             userMapper.updateBalance(totalFees);
        }else{
            //更新业主余额
            UpdateWrapper<Viechle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("parking_position_code", template.getParkingPositionCode());
            Viechle viechle = new Viechle();
            viechle.setBalance(viechle1.getBalance()+totalFees);
            viechleMapper.update(viechle, updateWrapper);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("totalFees",totalFees);
        map.put("time",time);
        return map;
    }



    /**
     *  计算停车时长
     * @param startTime
     * @return
     */
    private long calculateParkingDuration(Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long timeDifference = new Date().getTime() - startTime.getTime();
        long hour = timeDifference / nh;
        //停车时长不满1小时的按1小时计算
        if(hour==0){
            hour=1;
        }else{
            hour= (long) Math.ceil(hour);
        }
        return hour;
    }

    /**
     * 统计一天之内的车辆进出场的数据
     * @return
     */
    @Override
    public Map<String, Object> statistic() throws Exception {
        Map<String,Object> retun = new HashMap<String, Object>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.parse(sdf.format(date));
        //1.统计每个时间段进场数据的数量
         List<Map<String,Object>> com = baseMapper.statisticCom(date);
         retun.put("com",setValue(com,"num"));
        //2.统计每个时间段出场数据的数量
        List<Map<String,Object>> out = baseMapper.statisticOut(date);
        retun.put("out",setValue(out,"num"));
        //获取时间配置
        retun.put("time",setValue(baseMapper.getConfig(),"time"));
        return retun;
    }
    private List<Object> setValue(List<Map<String,Object>> list,String type) throws Exception {
        if(CollectionUtils.isEmpty(list)) throw new Exception("无数据");
        List<Object> result = new ArrayList<>();
        list.forEach(c->{
            result.add(c.get(type));
        });
        return result;
    }
}
