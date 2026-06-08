package grupo2.docubot.services;

import grupo2.docubot.exceptions.response.RecourseNotFound;
import grupo2.docubot.models.*;
import grupo2.docubot.repository.ChatAnalyzer;
import grupo2.docubot.dto.internal.AnalysisResponse;
import grupo2.docubot.repository.ChatRepository;
import grupo2.docubot.repository.UseCaseHistoryRepository;
import grupo2.docubot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroqService {

    private final ChatAnalyzer analyzer;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final UseCaseHistoryRepository useCaseHistoryRepository;

    @Transactional
    @Scheduled(fixedRate = 24 ,timeUnit = TimeUnit.HOURS)
    public void processDedicatedChats() {

        List<Chat> dedicatedChat = chatRepository.findAll().stream()
                                    .filter(c -> c.getIsDedicated()==true)
                                    .toList();


        //Recorro todos los chats dedicados
        for (Chat chat: dedicatedChat){
            // Creamos un instancia  UseCaseHistory para luego conservar el historial
            UseCaseHistory record = new UseCaseHistory();

            // Agarrando los mensajes de las ultimas 24 horas
            List<Message> messages = chat.getMessages();

            // Estandarizando los mensajes a un formato valido para procesar
            String standardizedMessages = normalizer(messages);

            //Generamos useCases
            AnalysisResponse analysis = analyzer.classify(standardizedMessages);

            //Seteamos los valores
            record.addHistory(chat.getId(),standardizedMessages);

            // Seteamos en los useCaseGenereados el historial
            for (UseCase useCase : analysis.getUse_cases()) {
                useCase.setId(null);
                record.addUseCase(useCase);
            }

            useCaseHistoryRepository.save(record);
        }
    }



    public String normalizer(List<Message> messages){
       return messages.stream()
                .map(m -> m.getUser().getFirstName()+ " " + m.getUser().getLastName() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));
    }

    public void addUseCases(AnalysisResponse analysis){

    }
}
