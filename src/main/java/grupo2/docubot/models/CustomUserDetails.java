package grupo2.docubot.models;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Lista de permisos o roles del individuo
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();

        //Añadiendo a la lista de authorities los roles del usuario
        user.getRole().forEach(rol->{

            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getName()));

            rol.getPermissions().forEach(permission->{
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
