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
public class Company {
    @Id
    @Column(name = "company_id")
    private int id;

    private String address;
    private String city;
    private String country;

    @Column(name = "created_at")
    private String created_at;

    private String industry;
    private double latitude;
    private double longitude;
    private String name;
    private String state;
    private int status;
    private String timezone;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "user_id")
    private String user_id;
    private String zip;

    @Column(name = "external_id")
    private String external_id;
}
