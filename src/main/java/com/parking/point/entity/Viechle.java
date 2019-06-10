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
 * 购买车位的用户表
 * </p>
 *
 * @author hui
 * @since 2019-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Viechle implements Serializable {

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
     * 余额
     */
    private Double balance;

    /**
     * 购买时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date effectedTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date expiredTime;

    /**
     * 时长
     */
    private Double totalDay;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 二次共享开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT")
    private Date sharingStartTime;

    /**
     * 二次共享结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT")
    private Date sharingEndTime;
    /**
     * 二次共享时长
     */
    private double sharingTotalDay;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否有效
     */
    private String isValid;


    /**
     * 车位状态
     */
    private String status;
}
