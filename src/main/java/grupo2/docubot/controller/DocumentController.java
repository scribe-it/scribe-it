package grupo2.docubot.controller;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.models.UseCase;
import grupo2.docubot.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentResponseDto>> getAll() {
        return ResponseEntity.ok().body(documentService.getAll());
    }

    @PostMapping
    public ResponseEntity<DocumentResponseDto> create(@RequestBody DocumentRequestDto documentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(documentRequestDto));
    }

    @PatchMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto> addUseCase(@PathVariable Long documentId, @RequestBody UseCase useCase) {
        return ResponseEntity.ok().body(documentService.addUseCase(documentId, useCase));
    }


}
