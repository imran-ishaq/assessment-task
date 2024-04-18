package com.assignment.parsing.repository;

import com.assignment.parsing.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDetailsRepository extends JpaRepository<FileDetails,Integer> {
    Optional<FileDetails> findByName(String name);
}
