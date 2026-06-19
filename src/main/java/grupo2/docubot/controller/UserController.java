package grupo2.docubot.controller;

import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import grupo2.docubot.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name="Usuario", description="Gestión del perfil de usuario logueado")
@PreAuthorize("hasRole('ANALISTA')")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve todos los usuarios registrados. Solo accesible para ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Actualizar nombre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/firstName")
    public ResponseEntity<UserResponseDto> updateFirstName(
            @PathVariable Long id,
            @RequestParam String firstName) {

        return ResponseEntity.ok(userService.updateUserFirstName(id, firstName));
    }

    @Operation(summary = "Actualizar apellido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Apellido actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/lastName")
    public ResponseEntity<UserResponseDto> updateLastName(
            @PathVariable Long id,
            @RequestParam String lastName) {

        return ResponseEntity.ok(userService.updateUserLastName(id, lastName));
    }

    @Operation(summary = "Obtener usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Datos del usuario"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }


}
