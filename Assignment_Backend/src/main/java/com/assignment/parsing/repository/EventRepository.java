package com.assignment.parsing.repository;

import com.assignment.parsing.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM event WHERE id = :eventId", nativeQuery = true)
    Event findEventById(Long eventId);
}
