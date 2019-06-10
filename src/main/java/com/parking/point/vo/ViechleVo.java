package com.parking.point.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ViechleVo implements Serializable {

    private static final long serialVersionUID = -8404370954625154252L;

    /**
     * id
     */
    private  String id;

    /**
     * 车牌号
     */
    private String viechleNumber;

    /**
     * 车位编号
     */
    private String parkingPositionCode;

    /**
     * 时长
     */
    private Double totalDay;
    /**
     * 起始日期
     */
    private Date effectiveTime;
    /**
     * 截至日期
     */
    private Date expiredTime;
}
