package grupo2.docubot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Anotacion que habilita el @PreAuthorize
public class SecurityConfig {

    private SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilitamos porque JWT nos protege de csrf
                .authorizeHttpRequests(auth-> auth

                        //endpoints que todos pueden acceder
                        .requestMatchers("api/**").permitAll()

                        //endpoints que solo el admin podra acceder
                        .requestMatchers("api/admin/**").hasRole("ANALISTA")

                        .anyRequest().authenticated()
                )


                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
