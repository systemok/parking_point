package com.parking.point.job;


import com.parking.point.entity.ParkingPoint;
import com.parking.point.service.IParkingPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledService {
@Autowired
   private IParkingPointService parkingPointService;
    /**
     * 每隔两分钟清理一次车位信息
     */
    @Scheduled(cron = "0 0/2 * * * ?")
      public void parkingClear(){
        log.info("---------开始清理车位------------");
         parkingPointService.check();
        log.info("---------清理车位结束------------");
            }

}
