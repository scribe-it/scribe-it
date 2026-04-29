package grupo2.docubot.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String user;
    private String content;
    private LocalDateTime messageDate;
}
