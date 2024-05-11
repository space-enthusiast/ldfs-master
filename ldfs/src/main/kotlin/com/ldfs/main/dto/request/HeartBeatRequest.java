package com.ldfs.main.dto.request;


import org.springframework.web.bind.annotation.RequestParam;

public class HeartBeatRequest {

    public String ip;
    public String port;
    public Long remainingStorageSize;

    public HeartBeatRequest() {
        //default constructor
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getRemainingStorageSize() {
        return remainingStorageSize;
    }

    public void setRemainingStorageSize(Long remainingStorageSize) {
        this.remainingStorageSize = remainingStorageSize;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
