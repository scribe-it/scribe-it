package grupo2.docubot.controller;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.services.UseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/use_cases")
@RequiredArgsConstructor
public class UseCaseController {

    private final UseCaseService useCaseService;

    @GetMapping
    public ResponseEntity<List<UseCaseResponseDto>> getAllUseCases() {
        return ResponseEntity.ok().body(useCaseService.findAll());
    }

    @PutMapping("/{useCaseId}")
    public ResponseEntity<UseCaseResponseDto> updateUseCase(@PathVariable Long useCaseId, @RequestBody UseCaseRequestDto useCase) {
        return ResponseEntity.ok().body(useCaseService.update(useCaseId, useCase));
    }

    @DeleteMapping("/{useCaseId}")
    public ResponseEntity<Void> deleteUseCase(@PathVariable Long useCaseId) {
        useCaseService.deleteById(useCaseId);
        return ResponseEntity.noContent().build();
    }

}
