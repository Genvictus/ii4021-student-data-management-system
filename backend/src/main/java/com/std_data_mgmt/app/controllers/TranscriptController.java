package com.std_data_mgmt.app.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/transcripts")
@AllArgsConstructor
public class TranscriptController<T> {
    private final TranscriptService transcriptService;

    @GetMapping("/student/{id}")
    public FormattedResponseEntity<Optional<TranscriptDto>> getByStudentId(@PathVariable("id") String studentId) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok",
                this.transcriptService.findByStudentId(studentId));
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<Optional<TranscriptDto>> getById(@PathVariable("id") String id) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", this.transcriptService.getTranscriptById(id)
                .map(t -> t.toDto(false, false)));
    }

    @RequiresRole(value = { Role.HOD })
    @PatchMapping("/{id}/{signature}")
    public FormattedResponseEntity<Object> updateSignature(@PathVariable("id") String id,
            @PathVariable("id") String signature) {
        this.transcriptService.signTranscript(id, signature);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }
}
