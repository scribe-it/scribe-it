package grupo2.docubot.services;

import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.exceptions.response.UserNotFoundException;
import grupo2.docubot.mappers.UserMapper;
import grupo2.docubot.models.User;
import grupo2.docubot.models.Role;
import grupo2.docubot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User findAnalyst() {
        return userRepository.findByRole("ANALISTA")
                .orElseThrow(() -> new UserNotFoundException(("User analyst not found")));
    }

    @Transactional
    public UserResponseDto updateUserFirstName(Long id, String firstName) {
        User user = findById(id);
        user.setFirstName(firstName);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public UserResponseDto updateUserLastName(Long id, String lastName) {
        User user = findById(id);
        user.setLastName(lastName);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Transactional
    public UserResponseDto updateUserRole(Long id, Role newRole) {
        User user = findById(id);
        user.getRole().add(newRole);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @PreAuthorize("hasRole('ANALISTA')")
    @Transactional
    public UserResponseDto updateUserDepartment(Long id, String newDepartment) {
        User user = findById(id);
        user.setDepartment(newDepartment);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);

    }

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> getByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }
}
