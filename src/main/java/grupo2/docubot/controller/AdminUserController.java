package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import grupo2.docubot.mappers.RegisterMapper;
import grupo2.docubot.models.UseCase;
import grupo2.docubot.services.AuthService;
import grupo2.docubot.services.UseCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALISTA')")
public class AdminUserController {
    private final AuthService authService;
    private final RegisterMapper registerMapper;
    private final UseCaseService useCaseService;

    @PostMapping
     public ResponseEntity<UserResponseRegisterDto>createUser(@Valid @RequestBody UserRegisterRequestDto registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerByAdmin(registerRequest));
    }


    @GetMapping("/use-case/{historyId}/messages")
    public ResponseEntity<String> extractMessagesByUseCase(@PathVariable Long historyId){
        return ResponseEntity.ok(useCaseService.extractMessagesByUseCase(historyId));
    }

    @PatchMapping("/use-case/{historyId}/update")
    public ResponseEntity<UseCaseResponseDto> updateUseCase(@PathVariable Long historyId, @RequestBody UseCaseRequestDto useCaseRequestDto){
        return ResponseEntity.ok(useCaseService.update(historyId,useCaseRequestDto));
    }
}
