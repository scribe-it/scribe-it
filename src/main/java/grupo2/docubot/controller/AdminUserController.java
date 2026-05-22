package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import grupo2.docubot.mappers.RegisterMapper;
import grupo2.docubot.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALISTA')")
public class AdminUserController {
    private final AuthService authService;
    private final RegisterMapper registerMapper;

    @PostMapping
     public ResponseEntity<UserResponseRegisterDto>createUser(@Valid @RequestBody UserRegisterRequestDto registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerByAdmin(registerRequest));
    }
}
