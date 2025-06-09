package com.std_data_mgmt.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;

public interface TranscriptAccessInquiryRepository extends JpaRepository<TranscriptAccessInquiry, String> {
    @Query("select i from #{#entityName} i join fetch i.requestee where i.inquiryId = ?1")
    Optional<TranscriptAccessInquiry> findWithRequestee(String inquiryId);
}
