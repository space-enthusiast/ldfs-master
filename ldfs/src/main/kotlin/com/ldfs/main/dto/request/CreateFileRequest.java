package com.ldfs.main.dto.request;


import java.util.UUID;

public class CreateFileRequest {
    public UUID fileUuid;
    public Long fileSize;
    public String fileName;
    public UUID directoryId;

    public CreateFileRequest () {
        //default constructor
    }

    public UUID getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(UUID fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public UUID getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(UUID directoryId) {
        this.directoryId = directoryId;
    }
}
