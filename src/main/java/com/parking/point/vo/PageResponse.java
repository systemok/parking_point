package com.parking.point.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResponse<T> implements Serializable{

    private static final long serialVersionUID = -8909742841040336897L;

    private String code;

    private String msg;

    private Integer count;

    private List<T> data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
