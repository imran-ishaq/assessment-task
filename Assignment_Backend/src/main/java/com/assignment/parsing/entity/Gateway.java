package com.assignment.parsing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Gateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gweui;
    private int time;
    private int rssi;
    private double snr;
    @ManyToOne
    @JoinColumn(name = "event_data_id")
    private EventData eventData;
}
