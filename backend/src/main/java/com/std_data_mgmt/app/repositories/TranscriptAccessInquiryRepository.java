package com.std_data_mgmt.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;

public interface TranscriptAccessInquiryRepository extends JpaRepository<TranscriptAccessInquiry, String> {

}
