package com.std_data_mgmt.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.TranscriptAccessInquiryDto;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/transcripts/access-inquiries")
@AllArgsConstructor
public class TranscriptAccessInquiryController {

    private final TranscriptService transcriptService;

    @RequiresRole(value = { Role.SUPERVISOR, Role.HOD })
    @PostMapping()
    public FormattedResponseEntity<TranscriptAccessInquiryDto> createTranscriptAccessInquiry(
            @RequestBody String transcriptId, HttpServletRequest request) {
        var userId = (String) request.getAttribute("userId");
        var departmentId = (String) request.getAttribute("departmentId");

        var transcriptAccessInquiry = this.transcriptService.createTranscriptAccessInquiry(
                transcriptId,
                userId,
                departmentId).toDto();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", transcriptAccessInquiry);
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @GetMapping()
    public FormattedResponseEntity<List<TranscriptAccessInquiryDto>> getTranscriptAccessInquiries() {
        var inquiries = this.transcriptService.getTranscriptAccessInquiries();
        var inquiriesDto = inquiries.stream().map(i -> i.toDto()).toList();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", inquiriesDto);
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @PostMapping("/{inquiryId}/join")
    public FormattedResponseEntity<Object> joinTranscriptAccessInquiry(
            @PathVariable("inquiryId") String inquiryId,
            HttpServletRequest request) {

        var userId = (String) request.getAttribute("userId");
        var departmentId = (String) request.getAttribute("departmentId");

        this.transcriptService.joinTranscriptAccessInquiry(inquiryId, userId, departmentId);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @PostMapping("/{inquiryId}/approve")
    public FormattedResponseEntity<Object> approveTranscriptAccessInquiry(
            @PathVariable("inquiryId") String inquiryId,
            @RequestBody Map<String, String> encryptedShares) {

        this.transcriptService.approveTranscriptAccessInquiry(inquiryId, encryptedShares);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @PostMapping("/{inquiryId}/reject")
    public FormattedResponseEntity<Object> rejectTranscriptAccessInquiry(@PathVariable("inquiryId") String inquiryId) {
        this.transcriptService.rejectTranscriptAccessInquiry(inquiryId);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }
}
