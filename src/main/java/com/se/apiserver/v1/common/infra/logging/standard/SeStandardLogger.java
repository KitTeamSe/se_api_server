package com.se.apiserver.v1.common.infra.logging.standard;

import com.se.apiserver.v1.common.infra.logging.SeLogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class SeStandardLogger implements SeLogger {
  private final Logger logger;

  public SeStandardLogger() {
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  public void error(String className, String msg) {
    logger.error("{} : {}", className, msg);
  }

  @Override
  public void error(String className, String msg, Throwable t) {
    logger.error("{} : {}", className, msg, t);
  }

  @Override
  public void warn(String className, String msg) {
    logger.warn("{} : {}", className, msg);
  }

  @Override
  public void warn(String className, String msg, Throwable t) {
    logger.warn("{} : {}", className, msg, t);
  }

  @Override
  public void info(String className, String msg) {
    logger.info("{} : {}", className, msg);
  }

  @Override
  public void info(String className, String msg, Throwable t) {
    logger.info("{} : {}", className, msg, t);
  }

  @Override
  public void debug(String className, String msg){
    if(logger.isDebugEnabled()){
      logger.debug("{} : {}", className, msg);
    }
  }

  @Override
  public void debug(String className, String msg, Throwable t) {
    if(logger.isDebugEnabled()){
      logger.debug("{} : {}", className, msg, t);
    }
  }
}