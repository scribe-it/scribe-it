package grupo2.docubot.models;

import grupo2.docubot.models.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT", nullable = false) // Definimos como TEXT asi puede sobrepasar los 255 caracteres
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    private Boolean isRead;

    private LocalDateTime isReadAt;

    private Boolean isProcessed;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
        if(this.isRead == null) this.isRead = false;
        if(this.isProcessed == null) this.isProcessed = false;
    }
}
