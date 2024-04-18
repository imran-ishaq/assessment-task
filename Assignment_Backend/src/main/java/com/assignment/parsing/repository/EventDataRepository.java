package com.assignment.parsing.repository;

import com.assignment.parsing.entity.EventData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDataRepository extends JpaRepository<EventData, Long> {
}
