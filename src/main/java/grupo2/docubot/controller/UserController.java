package grupo2.docubot.controller;

import grupo2.docubot.models.User;
import grupo2.docubot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return service.getById(id);
    }
}
