package com.assignment.parsing.repository;

import com.assignment.parsing.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileDetailsRepository extends JpaRepository<FileDetails,Integer> {
    @Query("SELECT f FROM FileDetails f WHERE f.name = :name ORDER BY f.last_modified DESC LIMIT 1")
    Optional<FileDetails> findLatestByName(@Param("name") String name);                 // This method retrieves the details of latest file from the db.
}
