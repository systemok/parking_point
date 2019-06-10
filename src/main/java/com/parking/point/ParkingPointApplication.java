package com.parking.point;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.parking.point.mapper")
public class ParkingPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingPointApplication.class, args);
    }

}
