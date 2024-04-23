package com.assignment.parsing.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventDataDTO {
    private Long id;
    private String correlation_id;
    private String device_id;
    private String user_id;
    private List<SensorReadingDTO> payload;
    private List<GatewayDTO> gateways;
    private int fcnt;
    private int fport;
    private String raw_payload;
    private String raw_format;
    private String client_id;
    private String hardware_id;
    private long timestamp;
    private String application_id;
    private String device_type_id;
    private int lora_datarate;
    private double freq;
}
