package com.assignment.parsing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorReadingDTO {
    private Long id;
    private String name;
    private String sensor_id;
    private String type;
    private String unit;
    private double value;
    private int channel;
    private long timestamp;
}
