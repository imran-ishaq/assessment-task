package com.assignment.parsing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GatewayDTO {
    private Long id;
    private String gweui;
    private int time;
    private int rssi;
    private double snr;
}
