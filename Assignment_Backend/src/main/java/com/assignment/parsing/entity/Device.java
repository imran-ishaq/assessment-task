package com.assignment.parsing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Device {
    @Id
    private int id;

    @Column(name = "thing_name")
    private String thing_name;

    @Column(name = "sensor_use")
    private String sensor_use;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    private int status;

    @Column(name = "external_id")
    private String external_id;
}
