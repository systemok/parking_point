package com.parking.point.service;

import com.parking.point.entity.Template;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 临时用户表 服务类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface ITemplateService extends IService<Template> {

    /**
     * 车辆驶出计费
     *
     * @param id
     * @return
     */
    Map<String, Object> vehicleLeaving(Integer id);

    Map<String,Object> statistic() throws Exception;
}
