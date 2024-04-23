package com.assignment.parsing.dto;

import com.assignment.parsing.entity.Company;
import com.assignment.parsing.entity.Device;
import com.assignment.parsing.entity.DeviceType;
import com.assignment.parsing.entity.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private String event_type;
    private EventDataDTO event_data;
    private Company company;
    private Location location;
    private DeviceType device_type;
    private Device device;
}
