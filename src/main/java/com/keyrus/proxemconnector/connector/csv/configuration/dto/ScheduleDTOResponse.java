package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ScheduleDTOResponse {
   // Collection<ProxemDto> proxemDtos;
    private boolean success;
    private String jobId;
    private String jobGroup;
    private String message;
    public ScheduleDTOResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScheduleDTOResponse(boolean success, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;

    }
}
