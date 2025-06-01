package com.std_data_mgmt.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.std_data_mgmt.app.entities.Transcript;

public interface TranscriptRepository extends JpaRepository<Transcript, String> {

}
