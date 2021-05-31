package com.se.apiserver.v1.notice.infra.dto;

import lombok.Builder;

import java.util.List;

public class SendEntity {
    private List<Long> accountIdList;
    private String title;
    private String message;
    private String url;

    public List<Long> getAccountIdList() {
        return accountIdList;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public void setAccountIdList(List<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Builder
    public SendEntity(List<Long> accountIdList, String title, String message, String url) {
        this.accountIdList = accountIdList;
        this.title = title;
        this.message = message;
        this.url = url;
    }
}