package com.parking.point.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.Viechle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.parking.point.vo.ViechleVo;

/**
 * <p>
 * 购买车位的用户表 服务类
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface IViechleService extends IService<Viechle> {

    Viechle getViechleByNo(String viechleNumber);

    void clean();

    void buyAgain(ViechleVo viechleVo) throws Exception;

    IPage<Viechle> listViechle(Page<Viechle> parkingPointPage);

    void share(ViechleVo viechleVo) throws Exception;
}
