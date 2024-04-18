package com.assignment.parsing.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"payload", "gateways"})
public class EventData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlation_id")
    private String correlation_id;

    @Column(name = "device_id")
    private String device_id;

    @Column(name = "user_id")
    private String user_id;

    @OneToMany(mappedBy = "eventData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorReading> payload;

    @OneToMany(mappedBy = "eventData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gateway> gateways;

    private int fcnt;

    private int fport;

    @Column(name = "raw_payload")
    private String raw_payload;

    @Column(name = "raw_format")
    private String raw_format;

    @Column(name = "client_id")
    private String client_id;

    @Column(name = "hardware_id")
    private String hardware_id;

    private long timestamp;

    @Column(name = "application_id")
    private String application_id;

    @Column(name = "device_type_id")
    private String device_type_id;

    @Column(name = "lora_datarate")
    private int lora_datarate;

    private double freq;
}
