package com.parking.point.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateVo implements Serializable {

    private static final long serialVersionUID = -5380099912901452762L;

    private String viechleNumber;

    private String parkingPositionCode;

    private double fees;
}
