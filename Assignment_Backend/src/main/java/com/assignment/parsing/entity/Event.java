package com.assignment.parsing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private String event_type;

    @ManyToOne
    @JoinColumn(name = "event_data_id")
    private EventData event_data;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "device_type_id")
    private DeviceType device_type;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
}
