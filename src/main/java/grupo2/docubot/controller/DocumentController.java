package grupo2.docubot.controller;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.services.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/drafts")
    public ResponseEntity<List<DocumentResponseDto>> getAllDrafts() {
        return ResponseEntity.ok().body(documentService.getAllDrafts());
    }

    @PostMapping
    public ResponseEntity<DocumentResponseDto> create(@RequestBody @Valid DocumentRequestDto documentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(documentRequestDto));
    }

    @PatchMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto> addUseCase(@PathVariable Long documentId, @RequestBody @Valid UseCaseRequestDto useCase) {
        return ResponseEntity.ok().body(documentService.addUseCase(documentId, useCase));
    }

    @PatchMapping("/{documentId}/remove/{useCaseId}")
    public ResponseEntity<Void> removeUseCase(@PathVariable Long documentId, @PathVariable Long useCaseId) {
        documentService.removeUseCase(documentId, useCaseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{documentId}/publish")
    public ResponseEntity<DocumentResponseDto> publishDraft(@PathVariable Long documentId) {
        return ResponseEntity.ok().body(documentService.publishDocument(documentId));
    }

    @PatchMapping("/{documentId}/unpublish")
    public ResponseEntity<DocumentResponseDto> unpublishDocument(@PathVariable Long documentId){
        return ResponseEntity.ok().body(documentService.unpublishDocument(documentId));
    }

    @GetMapping("/published")
    public ResponseEntity<List<DocumentResponseDto>> getAllPublished() {
        return ResponseEntity.ok().body(documentService.getAllPublished());
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDraft(@PathVariable Long documentId) {
        documentService.deleteDraft(documentId);
        return ResponseEntity.noContent().build();
    }


}
