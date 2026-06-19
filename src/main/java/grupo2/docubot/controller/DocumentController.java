package grupo2.docubot.controller;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Documentos", description="Gestión de documentos: creación, publicación, borradores y eliminación")
@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
    public class DocumentController {

    private final DocumentService documentService;

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Obtener todos los borradores")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de borradores del usuario autenticado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/drafts")
    public ResponseEntity<List<DocumentResponseDto>> getAllDrafts() {
        return ResponseEntity.ok().body(documentService.getAllDrafts());
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Crear borrador")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Borrador creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<DocumentResponseDto> create(@RequestBody @Valid DocumentRequestDto documentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(documentRequestDto));
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Agregar caso de uso al documento", description = "Asocia un caso de uso existente a un documento en estado borrador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Caso de uso agregado al borrador"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento o caso de uso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto> addUseCase(@PathVariable Long documentId, @RequestBody @Valid UseCaseRequestDto useCase) {
        return ResponseEntity.ok().body(documentService.addUseCase(documentId, useCase));
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Remover caso de uso del documento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Caso de uso removido del borrador (sin contenido)"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento o caso de uso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{documentId}/remove/{useCaseId}")
    public ResponseEntity<Void> removeUseCase(@PathVariable Long documentId, @PathVariable Long useCaseId) {
        documentService.removeUseCase(documentId, useCaseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Publicar borrador", description = "Cambia el estado del documento de borrador a publicado, haciéndolo visible para todos los usuarios")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento publicado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - el documento ya está publicado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{documentId}/publish")
    public ResponseEntity<DocumentResponseDto> publishDraft(@PathVariable Long documentId) {
        return ResponseEntity.ok().body(documentService.publishDocument(documentId));
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Eliminar publicación", description = "Revierte un documento publicado a estado borrador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Publicación revertida a borrador"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida - el documento no está publicado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{documentId}/unpublish")
    public ResponseEntity<DocumentResponseDto> unpublishDocument(@PathVariable Long documentId){
        return ResponseEntity.ok().body(documentService.unpublishDocument(documentId));
    }

    @Operation(summary = "Obtener todos los documentos publicados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de documentos publicados"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/published")
    public ResponseEntity<List<DocumentResponseDto>> getAllPublished() {
        return ResponseEntity.ok().body(documentService.getAllPublished());
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Eliminar borrador")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Borrador eliminado (sin contenido)"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - no se puede eliminar un documento publicado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDraft(@PathVariable Long documentId) {
        documentService.deleteDraft(documentId);
        return ResponseEntity.noContent().build();
    }


}