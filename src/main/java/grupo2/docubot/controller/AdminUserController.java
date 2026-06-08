package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.UseCaseMessageResponseDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import grupo2.docubot.models.*;
import grupo2.docubot.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALISTA')")
public class AdminUserController {
    private final AuthService authService;
    private final GroqService groqService;
    private final UseCaseHistoryService historyServices;
    private final UseCaseService useCaseService;
    private final UserService userService;

    @PostMapping
     public ResponseEntity<UserResponseRegisterDto>createUser(@Valid @RequestBody UserRegisterRequestDto registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerByAdmin(registerRequest));
    }

    //Funciino que realiza la genreacoin de CU
    @GetMapping("/{chatId}")
    public void test(@PathVariable Long chatId) {
        groqService.processDedicatedChats();
    }

    @GetMapping("/use_cases")
    public ResponseEntity<List<UseCase>> getUseCases(@AuthenticationPrincipal CustomUserDetails user){
        User currentUser = userService.findByEmail(user.getUsername());
        Chat dedicatedChat = currentUser.getChats().stream()
                .filter(c->c.getIsDedicated()==true)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("No posee chat dedicados"));
        UseCaseHistory history = historyServices.findByChatId(dedicatedChat.getId());
        return ResponseEntity.ok(historyServices.findUseCasesByHistory(history.getId()));
    }

    @GetMapping("/use_cases/detail/{useCaseId}")
    public ResponseEntity<UseCaseResponseDto> findUseCase(@PathVariable Long useCaseId){
        return ResponseEntity.ok(useCaseService.findById(useCaseId));
    }

    @GetMapping()
    public ResponseEntity<UseCaseMessageResponseDto> getUseCasesAndMessages(@PathVariable Long historyId){
        return ResponseEntity.ok(historyServices.seeDetailsUseCase(historyId));
    }

    @PutMapping("/use_cases/update/{useCaseId}")
    public ResponseEntity<UseCase> updateUseCase(@PathVariable Long useCaseId,@Valid @RequestBody UseCaseRequestDto useCaseRequestDto){
        return ResponseEntity.ok(useCaseService.update(useCaseId,useCaseRequestDto));
    }

}
