package grupo2.docubot.services;

import grupo2.docubot.dto.request.UserLoginRequestDto;
import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.AuthResponse;
import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.models.Role;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import grupo2.docubot.config.security.JwtService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse login(UserLoginRequestDto loginRequest) {

        //Validando Credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (loginRequest.getEmail(),loginRequest.getPassword())
        );

        //Recordamos que el usuario ya inicio sesion para su proxima peticion
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();

        //Generando token
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    public AuthResponse register(UserRegisterRequestDto registerRequest){

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .role(registerRequest.getRole())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .chats(new HashSet<>())
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userRepository.save(user));

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
