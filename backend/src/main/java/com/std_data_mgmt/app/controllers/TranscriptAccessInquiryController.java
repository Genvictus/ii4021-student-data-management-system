package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.TranscriptAccessInquiryDto;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.security.jwt.AuthenticatedUserInfo;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transcripts/access-inquiries")
@AllArgsConstructor
public class TranscriptAccessInquiryController {

    private final TranscriptService transcriptService;

    @RequiresRole(value = {Role.SUPERVISOR, Role.HOD})
    @PostMapping()
    public FormattedResponseEntity<TranscriptAccessInquiryDto> createTranscriptAccessInquiry(
            @RequestBody String transcriptId,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
    ) {
//       TODO: change request body with a separate DTO instead of raw string
        var userId = userInfo.getUserId();
        var departmentId = userInfo.getDepartmentId();

        var transcriptAccessInquiry = this.transcriptService.createTranscriptAccessInquiry(
                transcriptId,
                userId,
                departmentId
        ).toDto();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", transcriptAccessInquiry);
    }

    @RequiresRole(value = {Role.SUPERVISOR})
    @GetMapping()
    public FormattedResponseEntity<List<TranscriptAccessInquiryDto>> getTranscriptAccessInquiries() {
        var inquiries = this.transcriptService.getTranscriptAccessInquiries();
        var inquiriesDto = inquiries.stream().map(TranscriptAccessInquiry::toDto).toList();
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", inquiriesDto);
    }

    @RequiresRole(value = {Role.SUPERVISOR})
    @PostMapping("/{inquiryId}/join")
    public FormattedResponseEntity<Object> joinTranscriptAccessInquiry(
            @PathVariable("inquiryId") String inquiryId,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
    ) {
        var userId = userInfo.getUserId();
        var departmentId = userInfo.getDepartmentId();

        this.transcriptService.joinTranscriptAccessInquiry(inquiryId, userId, departmentId);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }

    @RequiresRole(value = {Role.SUPERVISOR})
    @PostMapping("/{inquiryId}/approve")
    public FormattedResponseEntity<Object> approveTranscriptAccessInquiry(
            @PathVariable("inquiryId") String inquiryId,
            @RequestBody Map<String, String> encryptedShares,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
    ) {
        // TODO: make a request DTO for this, don't use raw map as the request body
        // TODO (recommended): add validation with @Valid annotation
        var approverId = userInfo.getUserId();
        this.transcriptService.approveTranscriptAccessInquiry(inquiryId, encryptedShares, approverId);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }

    @RequiresRole(value = {Role.SUPERVISOR})
    @PostMapping("/{inquiryId}/reject")
    public FormattedResponseEntity<Object> rejectTranscriptAccessInquiry(
            @PathVariable("inquiryId") String inquiryId,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
    ) {
        var rejecterId = userInfo.getUserId();
        this.transcriptService.rejectTranscriptAccessInquiry(inquiryId, rejecterId);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }
}
