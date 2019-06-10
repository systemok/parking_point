package com.parking.point.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车位表
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ParkingPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 表id
     */
    @TableId(value = "ID", type = IdType.UUID)
    private String id;
    /**
     * 车位编号
     */
    private String code;

    /**
     * 是否共享 0 是 1 否
     */
    private String sharing;

    /**
     * 共享类型 1 长期 2 短期
     */
    private String sharingType;

    /**
     * 共享开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT")
    private Date sharingStartTime;

    /**
     * 共享结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT")
    private Date sharingEndTime;

    /**
     * 共享时长
     */
    private Double sharingCountDay;

    /**
     * 是否二次共享,0 否 1 是
     */
    private String sharingSecond;

    /**
     * 二次共享开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date sharingSecondStartTime;

    /**
     * 二次共享结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date sharingSecondEndTime;

    /**
     * 二次共享时长
     */
    private Double sharingSecondCountDay;

    /**
     * 距离
     */
    private Double distance;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 是否占用
     * 0:空闲，1：占用
     */
    private String isUsed;

}
