package grupo2.docubot.services;

import grupo2.docubot.models.User;
import grupo2.docubot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user){
        return  repository.save(user);
    }

    public User findById(Long id){
        return repository.findById(id)
                .orElseThrow(()->new RuntimeException("El User con id "+ id +" no fue encontrado"));
    }
}
