package grupo2.docubot.controller;

import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo2.docubot.models.User;
import grupo2.docubot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


    @PutMapping("/{id}/firstName")
    public ResponseEntity<UserResponseDto> updateFirstName(
            @PathVariable Long id,
            @RequestParam String firstName) {

        return ResponseEntity.ok(service.updateUserFirstName(id, firstName));
    }
  
    @PutMapping("/{id}/lastName")
    public ResponseEntity<UserResponseDto> updateLastName(
            @PathVariable Long id,
            @RequestParam String lastName) {

        return ResponseEntity.ok(service.updateUserLastName(id, lastName));
    }
  
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return service.findById(id);
    }
}
