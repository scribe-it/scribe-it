package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.services.UseCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Casos de uso", description="CRUD de casos de uso asociados a documentos")
@RestController
@RequestMapping("/api/v1/use_cases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALISTA')")
public class UseCaseController {

    private final UseCaseService useCaseService;

    @Operation(summary = "Obtener todos los casos de uso")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de todos los casos de uso"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<UseCaseResponseDto>> getAllUseCases() {
        return ResponseEntity.ok().body(useCaseService.findAll());
    }

    @Operation(summary = "Actualizar caso de uso")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Caso de uso actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Caso de uso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{useCaseId}")
    public ResponseEntity<UseCaseResponseDto> updateUseCase(@PathVariable Long useCaseId, @RequestBody UseCaseRequestDto useCase) {
        return ResponseEntity.ok().body(useCaseService.update(useCaseId, useCase));
    }

    @Operation(summary = "Eliminar caso de uso")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Caso de uso eliminado (sin contenido)"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Caso de uso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{useCaseId}")
    public ResponseEntity<Void> deleteUseCase(@PathVariable Long useCaseId) {
        useCaseService.deleteById(useCaseId);
        return ResponseEntity.noContent().build();
    }

}
