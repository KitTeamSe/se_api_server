package com.se.apiserver.v1.blacklist.infra.schedule;

import com.se.apiserver.v1.blacklist.application.service.BlacklistDeleteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BlacklistScheduleTask {

  private final BlacklistDeleteService blacklistDeleteService;

  public BlacklistScheduleTask(
      BlacklistDeleteService blacklistDeleteService) {
    this.blacklistDeleteService = blacklistDeleteService;
  }

  @Scheduled(cron = "${spring.blacklist.schedule.cron}", zone = "${spring.blacklist.schedule.zone}")
  public void release() {
    blacklistDeleteService.deleteExpired();
  }

}
