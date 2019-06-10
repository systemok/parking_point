package com.parking.point.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParkingVo implements Serializable {


    private static final long serialVersionUID = -2539382885908123048L;

    private String code;

    private Double distance;
}
