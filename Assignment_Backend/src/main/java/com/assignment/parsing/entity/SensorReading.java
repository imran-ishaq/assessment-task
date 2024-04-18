package com.assignment.parsing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "sensor_id")
    private String sensor_id;

    private String type;
    private String unit;
    private double value;
    private int channel;
    private long timestamp;
    @ManyToOne
    @JoinColumn(name = "event_data_id")
    private EventData eventData;
}


