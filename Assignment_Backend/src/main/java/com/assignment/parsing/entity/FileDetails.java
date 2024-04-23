package com.assignment.parsing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private Long event_id;
    private Date last_modified;

    public FileDetails(String name, Long event_id,String url,Date last_modified) {
        this.name = name;
        this.event_id = event_id;
        this.url = url;
        this.last_modified = last_modified;
    }
}
