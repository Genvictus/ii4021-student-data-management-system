package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.exceptions.ForbiddenException;
import com.std_data_mgmt.app.security.jwt.AuthenticatedUserInfo;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transcripts")
@AllArgsConstructor
public class TranscriptController<T> {
    private final TranscriptService transcriptService;

//    TODO: implement
//    @PostMapping()
//    public FormattedResponseEntity<TranscriptDto> createTranscript() {
//        Transcript transcript = Transcript.builder().
//        this.transcriptService.createTranscript()
//    }

//    TODO: implement
//    @GetMapping()
//    public FormattedResponseEntity<List<TranscriptDto>> getTranscripts(
//            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
//    ) {
//    Spec: get student transcripts (including the student) related to user role
//    if user role == STUDENT => get my own transcript
//    else if user role == SUPERVISOR => get my student's transcripts
//    else (HOD) => get student transcripts in my department
//    }


    @GetMapping("/id")
    public FormattedResponseEntity<String> getStudentTranscriptId(
            @RequestParam("studentId") String studentId
    ) {
        Optional<String> foundTranscriptId = this.transcriptService.getStudentTranscriptId(studentId);
        return foundTranscriptId.map(id -> new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully get student's transcript ID",
                id
        )).orElseGet(() -> new FormattedResponseEntity<>(
                HttpStatus.NOT_FOUND,
                false,
                "Transcript with student ID " + studentId + " not found",
                null
        ));
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<TranscriptDto> getById(
            @PathVariable("id") String id,
            @AuthenticationPrincipal AuthenticatedUserInfo userInfo
    ) {

        Role viewerRole = userInfo.getRole();
        String getterId = userInfo.getUserId();
        var transcript = this.transcriptService.getTranscriptById(id);

        if (transcript.isPresent() && !transcript.get().getStudent().getSupervisorId().equals(getterId)) {
            throw new ForbiddenException("You can only get your student's transcript");
        }

        var transcriptDto = transcript.map(t -> t.toDto(
                true,
                false,
                viewerRole
        ));

        return transcriptDto.map(dto -> new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully found transcript",
                dto
        )).orElseGet(() -> new FormattedResponseEntity<>(
                HttpStatus.NOT_FOUND,
                false,
                "Transcript with ID " + id + " not found",
                null
        ));
    }

//    @PutMapping("/{id}")
//    public FormattedResponseEntity<TranscriptDto> updateTranscript() {
//        spec: Update in a PUT fashion, don't forget to validate based on updaterId like above
//    }

    @RequiresRole(value = {Role.HOD})
    @PatchMapping("/{id}/signature")
    public FormattedResponseEntity<Object> updateSignature(
            @PathVariable("id") String id,
            String signature // TODO: make a DTO for this request so that signature will be in request body instead
    ) {
        this.transcriptService.signTranscript(id, signature);
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", null);
    }
}
