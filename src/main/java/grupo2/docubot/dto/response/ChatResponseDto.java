package grupo2.docubot.dto.response;
import java.util.List;

import lombok.*;

@Getter
@Setter
public class ChatResponseDto {

    private Long id;

    private String name;
    
    private String description;

    private List<MessageResponseDto> messages;

    private List<UserResponseDto> users;
    
}
