package com.std_data_mgmt.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.TranscriptUpdateDto;
import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.exceptions.ForbiddenException;
import com.std_data_mgmt.app.security.jwt.AuthenticatedUserInfo;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/transcripts")
@AllArgsConstructor
public class TranscriptController {
    private final TranscriptService transcriptService;

    @RequiresRole(value = { Role.STUDENT, Role.SUPERVISOR, Role.HOD })
    @GetMapping()
    public FormattedResponseEntity<List<TranscriptDto>> getTranscripts(
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {
        List<TranscriptDto> transcripts = switch (userInfo.getRole()) {
            case Role.STUDENT -> {
                Transcript transcript = this.transcriptService.findTranscriptByStudentId(userInfo.getUserId()).get();
                yield List.of(transcript.toDto(true, false, Role.STUDENT));
            }
            case Role.SUPERVISOR -> this.transcriptService.findTranscriptBySupervisor(userInfo.getUserId())
                    .stream()
                    .map(t -> t.toDto(true, false, Role.SUPERVISOR))
                    .toList();
            case Role.HOD -> this.transcriptService.findTranscriptByHod(userInfo.getUserId())
                    .stream()
                    .map(t -> t.toDto(true, false, Role.HOD))
                    .toList();
        };
        return new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                String.format("Transcript retrieved for role %s", userInfo.getRole().name()),
                transcripts);
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @GetMapping("/student/{id}")
    public FormattedResponseEntity<String> getStudentTranscriptId(
            @PathVariable("id") String studentId) {
        Optional<String> foundTranscriptId = this.transcriptService.getStudentTranscriptId(studentId);
        return foundTranscriptId.map(id -> new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully get student's transcript ID",
                id)).orElseGet(() -> new FormattedResponseEntity<>(
                        HttpStatus.NOT_FOUND,
                        false,
                        "Transcript with student ID " + studentId + " not found",
                        null));
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @PostMapping()
    public FormattedResponseEntity<TranscriptDto> createTranscript(
            @Valid @RequestBody TranscriptUpdateDto transcriptCreationDto,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {
        Transcript transcript = transcriptCreationDto.toTranscript();
        TranscriptDto createdTranscript = this.transcriptService
                .createTranscript(transcript, userInfo.getUserId(), userInfo.getDepartmentId())
                .toDto(false, false, userInfo.getRole());
        return new FormattedResponseEntity<TranscriptDto>(HttpStatus.OK, true, "Transcript created", createdTranscript);
    }

    @RequiresRole(value = { Role.SUPERVISOR, Role.HOD })
    @GetMapping("/{id}")
    public FormattedResponseEntity<TranscriptDto> getById(
            @PathVariable("id") String id,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {

        Role viewerRole = userInfo.getRole();
        String getterId = userInfo.getUserId();
        var transcript = this.transcriptService.getTranscriptById(id);

        // Punten ini dihapus dulu karena perlu beberapa data untuk nampilin transcript
        // setelah rekonstruksi
        // if (transcript.isPresent() &&
        // !transcript.get().getStudent().getSupervisorId().equals(getterId)) {
        // throw new ForbiddenException("You can only get your student's transcript");
        // }

        var transcriptDto = transcript.map(t -> t.toDto(
                true,
                false,
                viewerRole));

        return transcriptDto.map(dto -> new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully found transcript",
                dto)).orElseGet(() -> new FormattedResponseEntity<>(
                        HttpStatus.NOT_FOUND,
                        false,
                        "Transcript with ID " + id + " not found",
                        null));
    }

    @RequiresRole(value = { Role.SUPERVISOR })
    @PutMapping("/{id}")
    public FormattedResponseEntity<Object> updateTranscript(
            @PathVariable("id") String id,
            @Valid @RequestBody TranscriptUpdateDto transcript,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {
        this.transcriptService.updateTranscript(transcript.toTranscript());
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }

    @RequiresRole(value = { Role.HOD })
    @PatchMapping("/{id}/signature")
    public FormattedResponseEntity<Object> updateSignature(
            @PathVariable("id") String id,
            @RequestBody String signature // TODO: make a DTO for this request so that signature will be in
    // request body instead
    ) {
        this.transcriptService.signTranscript(id, signature);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }
}
