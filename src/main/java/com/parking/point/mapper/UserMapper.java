package com.parking.point.mapper;

import com.parking.point.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
public interface UserMapper extends BaseMapper<User> {

    void updateBalance(@Param("totalFees") double totalFees);
}
