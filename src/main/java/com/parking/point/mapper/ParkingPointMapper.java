package com.parking.point.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.point.entity.ParkingPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车位表 Mapper 接口
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface ParkingPointMapper extends BaseMapper<ParkingPoint> {
    List<ParkingPoint> notUsedParkingPoint();

      ParkingPoint getParkingPointByPPCode(String code);

    int updateStatus(@Param("parkingPointCode") String parkingPointCode, @Param("status") int status);

    List<ParkingPoint> list(@Param("mean") String mean);

    void cleanIsUsed();
    void cleanIsUsed1();
    void cleanSharing();

    IPage<ParkingPoint> listParkingPoint(Page<ParkingPoint> parkingPointPage);

    void removeByCode(@Param("code") String code);

    List<Map<String, Object>> statisticPointBuy();

    void cleanSharingSecond();
}
