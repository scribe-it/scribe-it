package grupo2.docubot.controller;

import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/firstName")
    public ResponseEntity<UserResponseDto> updateFirstName(
            @PathVariable Long id,
            @RequestParam String firstName) {

        return ResponseEntity.ok(userService.updateUserFirstName(id, firstName));
    }
    @PutMapping("/{id}/lastName")
    public ResponseEntity<UserResponseDto> updateLastName(
            @PathVariable Long id,
            @RequestParam String lastName) {

        return ResponseEntity.ok(userService.updateUserLastName(id, lastName));
    }
}
