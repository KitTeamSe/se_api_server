package com.se.apiserver.v1.deployment.domain.entity;

import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class DeploymentAlertMessage {

  private List<DeploymentErrorCode> alertErrors;

  public DeploymentAlertMessage(){
    alertErrors = new ArrayList<>();
  }

  public void addAlertMessages(DeploymentErrorCode alertError){
    this.alertErrors.add(alertError);
  }

  public boolean isEmpty(){
    return alertErrors.isEmpty();
  }
}
