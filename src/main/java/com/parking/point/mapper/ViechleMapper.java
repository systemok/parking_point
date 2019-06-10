package com.parking.point.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.Viechle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 购买车位的用户表 Mapper 接口
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface ViechleMapper extends BaseMapper<Viechle> {

    Viechle getViechleByNo(String viechleNumber);

    void cleanViechle();
    void cleanViechle1();

    IPage<Viechle> listViechle(Page<Viechle> parkingPointPage);
}
