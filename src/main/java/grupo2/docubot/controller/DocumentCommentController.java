package grupo2.docubot.controller;

import grupo2.docubot.dto.request.DocumentCommentRequestDto;
import grupo2.docubot.dto.response.DocumentCommentResponseDto;
import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.services.DocumentCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Comentarios", description="Comentarios asociados a documentos publicados")
@RestController
@RequestMapping("/api/v1/document/{documentId}/comments")
@RequiredArgsConstructor
public class DocumentCommentController {

    private final DocumentCommentService commentService;

    @Operation(summary = "Obtener comentarios del documento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de comentarios del documento"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DocumentCommentResponseDto>> getComments(@PathVariable Long documentId) {
        return ResponseEntity.ok(commentService.getComments(documentId));
    }

    @Operation(summary = "Agregar comentarios al documento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comentario agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
