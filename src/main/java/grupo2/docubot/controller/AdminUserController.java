package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import grupo2.docubot.services.AuthService;
import grupo2.docubot.services.UseCaseService;
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

@Tag(name="Misceláneas", description="Acciones específicas de analista que no encuadran en una entidad específica")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALISTA')")
public class AdminUserController {
    private final AuthService authService;
    private final UseCaseService useCaseService;

    @Operation(summary="Crear usuario", description="Registra un nuevo usuario con rol EXPERTO. Solo accesible para ANALISTA.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
     public ResponseEntity<UserResponseRegisterDto>createUser(@Valid @RequestBody UserRegisterRequestDto registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerByAdmin(registerRequest));
    }

    @Operation(summary = "Extraer mensajes del caso de uso", description = "Devuelve el historial de mensajes asociados a un caso de uso en formato texto. Solo accesible para ANALISTA.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mensajes extraídos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
        @ApiResponse(responseCode = "404", description = "Caso de uso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/use-case/{historyId}/messages")
    public ResponseEntity<String> extractMessagesByUseCase(@PathVariable Long historyId){
        return ResponseEntity.ok(useCaseService.extractMessagesByUseCase(historyId));
    }

}
