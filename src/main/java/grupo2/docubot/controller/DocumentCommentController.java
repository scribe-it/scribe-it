package grupo2.docubot.controller;

import grupo2.docubot.dto.request.DocumentCommentRequestDto;
import grupo2.docubot.dto.response.DocumentCommentResponseDto;
import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.services.DocumentCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document/{documentId}/comments")
@RequiredArgsConstructor
public class DocumentCommentController {

    private final DocumentCommentService commentService;

    @GetMapping
    public ResponseEntity<List<DocumentCommentResponseDto>> getComments(@PathVariable Long documentId) {
        return ResponseEntity.ok(commentService.getComments(documentId));
    }

    @PostMapping
    public ResponseEntity<DocumentCommentResponseDto> addComment(
            @PathVariable Long documentId,
            @RequestBody @Valid DocumentCommentRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(documentId, dto, user.getId()));
    }
}
