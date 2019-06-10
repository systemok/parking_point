package com.parking.point.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 临时用户表
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 表id
     */
    private int id;
    /**
     * 车牌号
     */
    private String viechleNumber;

    /**
     * 车位编号
     */
    private String parkingPositionCode;
    /**
     * 收费标准
     */
    private double fees;

    /**
     * 驶入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startTime;

    /**
     * 驶出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endTime;

    /**
     * 时长
     */
    private Double totalDay;

    /**
     * 总费用
     */
    private Double money;

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


}
