package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UserLoginRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.AuthResponseDto;
import grupo2.docubot.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto loginRequest){
        return ResponseEntity.ok(service.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegisterRequestDto registerRequest){
        return ResponseEntity.ok(service.register(registerRequest));
    }
}
