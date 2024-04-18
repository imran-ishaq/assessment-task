package com.assignment.parsing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeviceType {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "application_id")
    private String application_id;

    private String category;
    private String codec;

    @Column(name = "data_type")
    private String data_type;

    private String description;
    private String manufacturer;
    private String model;
    private String name;

    @Column(name = "parent_constraint")
    private String parent_constraint;

    @Column(name = "proxy_handler")
    private String proxy_handler;

    private String subcategory;

    @Column(name = "transport_protocol")
    private String transport_protocol;

    private String version;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;
}
