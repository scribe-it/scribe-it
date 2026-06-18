package grupo2.docubot.services;

import grupo2.docubot.dto.request.UserLoginRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.AuthResponseDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import grupo2.docubot.exceptions.response.InvalidOperationException;
import grupo2.docubot.mappers.RegisterMapper;
import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.models.Role;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.RoleRepository;
import grupo2.docubot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthResponseDto login(UserLoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (loginRequest.getEmail(),loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDto(
                token,
                userDetails.getRoleName(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getDepartment(),
                userDetails.getFirstName(),
                userDetails.getLastName()
        );
    }

    public AuthResponseDto register(UserRegisterRequestDto registerRequest){
        List<Role> roles = roleRepository.findAllById(registerRequest.getRoleId());
        System.out.println(roles);

        if (roles.isEmpty()) {
            throw new InvalidOperationException("Debe seleccionar al menos un rol válido.");
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .role(new HashSet<>(roles))
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .chats(new HashSet<>())
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userRepository.save(user));

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDto(
                token,
                userDetails.getRoleName(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getDepartment(),
                userDetails.getFirstName(),
                userDetails.getLastName()
        );
    }

    public UserResponseRegisterDto registerByAdmin(UserRegisterRequestDto registerRequest){
        List<Role> roles = roleRepository.findAllById(registerRequest.getRoleId());

        if (roles.isEmpty()) {
            throw new InvalidOperationException("Debe seleccionar al menos un rol válido.");
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .role(new HashSet<>(roles))
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .department(registerRequest.getDepartment())
                .chats(new HashSet<>())
                .build();


        userRepository.save(user);

        return registerMapper.toDto(registerRequest);
    }


}
