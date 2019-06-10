package com.parking.point.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnore;
import java.io.Serializable;

/**
 * Created by xiaolan on 2018/1/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化JSON的时候，如果是NULL的对象，KEY也会消失
public class ServerResponse<T> implements Serializable {

     private int status;
    private  String msg;
    private  T data;

    private ServerResponse(int status){
        this.status=status;

    }
    private ServerResponse(int status, T data){
        this.status=status;
        this.data=data;

    }
    private ServerResponse(int status, String msg, T data){
        this.status=status;
        this.msg=msg;
        this.data=data;

    }
    private ServerResponse(int status, String msg){
        this.status=status;
        this.msg=msg;


    }
    @JsonIgnore
    //使之不在JSON序列化之中
    public  boolean isSuccess(){

        return this.status==ResponseCode.SUCCESS.getCode();
    }

    public  int getStatus(){
        return status;
    }
    public  T getData(){
        return data;
    }
    public  String getMsg(){
        return msg;
    }
    public  static  <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public  static  <T> ServerResponse<T> createBySuccess(boolean flag){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public  static   <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public  static   <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public  static   <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public  static   <T> ServerResponse<T> createBySuccessMessage(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public  static   <T> ServerResponse<T> createBySuccessMessage(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public  static  <T> ServerResponse<T> createByError(){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public  static  <T> ServerResponse<T> createByErrorMessage(String ErrorMessage){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),ErrorMessage);
    }
    public  static  <T> ServerResponse<T> createByErrorCodeMessage(int ErrorCode,String ErrorMessage){
        return  new ServerResponse<T>(ErrorCode,ErrorMessage);
    }
}
