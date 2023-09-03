package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleDTORequestJDBC {
    private String cronExpression;
    //les restes des attriut du scheduler

    @NotNull
    @JsonProperty
    ConnectorJDBCDAO connectorDAO;

    @NotNull
    private LocalDateTime dateTime;

    private LocalDateTime endTime;

    @NotNull
    private ZoneId timeZone;
}
