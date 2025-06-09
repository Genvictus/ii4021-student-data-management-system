package com.std_data_mgmt.app.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;

@RestController
@RequestMapping("api/v1/transcripts")
public class TranscriptController {
    private final TranscriptService transcriptService;

    public TranscriptController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<Optional<TranscriptDto>> getById(@PathVariable("id") String id) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", this.transcriptService.getTranscriptById(id)
                .map(t -> t.toDto(false, false)));
    }

    @GetMapping("/student/{id}")
    public FormattedResponseEntity<Optional<TranscriptDto>> getByStudentId(@PathVariable("id") String studentId) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok",
                this.transcriptService.findByStudentId(studentId));
    }
}
