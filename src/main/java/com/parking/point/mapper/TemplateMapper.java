package com.parking.point.mapper;

import com.parking.point.entity.Template;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 临时用户表 Mapper 接口
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface TemplateMapper extends BaseMapper<Template> {

    List<Map<String, Object>> statisticCom(@Param("date")Date date);

    List<Map<String, Object>> statisticOut(@Param("date") Date date);

    List<Map<String, Object>> getConfig();
}
