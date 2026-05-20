package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @OneToMany()
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "chat_users",
        joinColumns = @JoinColumn(name="chat_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<User> users = new ArrayList<>();

}

