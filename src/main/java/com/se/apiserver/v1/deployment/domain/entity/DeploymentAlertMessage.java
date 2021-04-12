package com.se.apiserver.v1.deployment.domain.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class DeploymentAlertMessage {

  private List<String> alertMessages;

  public DeploymentAlertMessage(){
    alertMessages = new ArrayList<>();
  }

  public void addAlertMessages(String alertMessage){
    this.alertMessages.add(alertMessage);
  }

  public boolean isEmpty(){
    return alertMessages.isEmpty();
  }
}
