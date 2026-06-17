package grupo2.docubot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UseCaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    @Column(columnDefinition = "TEXT")
    private String messages;
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "history" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UseCase> useCases=new ArrayList<>();
    @OneToMany(mappedBy = "history",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages=new ArrayList<>();

    
    public void addUseCase(UseCase useCase){
        this.useCases.add(useCase);
        useCase.setHistory(this);
    }

    public void addHistory(Long chatId,List<MessageResponseDto> processedMessages ){
        this.setChatId(chatId);
        for(Message msg : processedMessages){
            msg.setHistory(this); 
            this.messages.add(msg);
        }
        this.setCreatedAt(LocalDateTime.now());
    }
}
