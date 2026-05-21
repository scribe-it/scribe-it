package grupo2.docubot.services;

import grupo2.docubot.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getById(Long id);
}
