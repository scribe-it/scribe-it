package grupo2.docubot.services;

import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.mappers.UserMapper;
import grupo2.docubot.models.User;
import grupo2.docubot.models.enums.Role;
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
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
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

    @PreAuthorize("hasRole('ANALIST')")
    @Transactional
    public UserResponseDto updateUserRole(Long id, Role newRole) {
        User user = findById(id);
        user.setRole(newRole);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @PreAuthorize("hasRole('ANALIST')")
    @Transactional
    public UserResponseDto updateUserDepartment(Long id, String newDepartment) {
        User user = findById(id);
        user.setDepartment(newDepartment);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    private final UserRepository repository;

    public User save(User user){
        return  repository.save(user);
    }

    public List<User> getByDepartment(String department) {
        return repository.findByDepartment(department);
    }
}
