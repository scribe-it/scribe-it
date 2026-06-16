package grupo2.docubot.dto.response;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDto {
    private String token;
    private Long userId;
    private String username;
    private String department;
}
