package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UserLoginRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.AuthResponseDto;
import grupo2.docubot.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Autenticación", description="Registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña, devuelve un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso, devuelve token JWT y datos del usuario"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto loginRequest){
        return ResponseEntity.ok(service.login(loginRequest));
    }

//    @Operation(summary = "Registro")
//    @PostMapping("/register")
//    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegisterRequestDto registerRequest){
//        return ResponseEntity.ok(service.register(registerRequest));
//    }
}
