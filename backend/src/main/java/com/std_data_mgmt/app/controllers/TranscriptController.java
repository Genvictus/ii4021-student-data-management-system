package com.std_data_mgmt.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.TranscriptCreationDto;
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
public class TranscriptController<T> {
    private final TranscriptService transcriptService;

    @PostMapping()
    public FormattedResponseEntity<TranscriptDto> createTranscript(
            @Valid @RequestBody TranscriptCreationDto transcriptCreationDto,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {
        Transcript transcript = transcriptCreationDto.toTranscript();
        TranscriptDto createdTranscript = this.transcriptService
                .createTranscript(transcript, userInfo.getUserId(), userInfo.getDepartmentId())
                .toDto(false, false, userInfo.getRole());
        return new FormattedResponseEntity<TranscriptDto>(HttpStatus.OK, true, "Transcript created", createdTranscript);
    }

    // TODO: implement
    @GetMapping()
    public FormattedResponseEntity<List<TranscriptDto>> getTranscripts(
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {
        List<TranscriptDto> transcripts;
        switch (userInfo.getRole()) {
            case Role.STUDENT:
                Transcript transcript = this.transcriptService.findTranscriptByStudentId(userInfo.getUserId()).get();
                transcripts = List.of(transcript.toDto(false, false, null));
                break;
            case Role.SUPERVISOR:
                transcripts = this.transcriptService.findTranscriptBySupervisor(userInfo.getUserId())
                        .stream()
                        .map(t -> t.toDto(false, false, Role.SUPERVISOR))
                        .toList();
                break;
            case Role.HOD:
                transcripts = List.of();
                break;
            default:

                transcripts = List.of();
                break;
        }
        return new FormattedResponseEntity<>(HttpStatus.OK, false, null, transcripts);
    }
    // Spec: get student transcripts (including the student) related to user role
    // if user role == STUDENT => get my own transcript
    // else if user role == SUPERVISOR => get my student's transcripts
    // else (HOD) => get student transcripts in my department
    // }

    @GetMapping("/id")
    public FormattedResponseEntity<String> getStudentTranscriptId(
            @RequestParam("studentId") String studentId) {
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

    @GetMapping("/{id}")
    public FormattedResponseEntity<TranscriptDto> getById(
            @PathVariable("id") String id,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo) {

        Role viewerRole = userInfo.getRole();
        String getterId = userInfo.getUserId();
        var transcript = this.transcriptService.getTranscriptById(id);

        if (transcript.isPresent() && !transcript.get().getStudent().getSupervisorId().equals(getterId)) {
            throw new ForbiddenException("You can only get your student's transcript");
        }

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

    // @PutMapping("/{id}")
    // public FormattedResponseEntity<TranscriptDto> updateTranscript() {
    // spec: Update in a PUT fashion, don't forget to validate based on updaterId
    // like above
    // }

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
