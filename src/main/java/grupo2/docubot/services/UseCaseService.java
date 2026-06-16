package grupo2.docubot.services;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.exceptions.response.RecourseNotFound;
import grupo2.docubot.exceptions.response.ResourceNotFound;
import grupo2.docubot.mappers.UseCaseMapper;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.UseCase;
import grupo2.docubot.models.UseCaseHistory;
import grupo2.docubot.repository.UseCaseHistoryRepository;
import grupo2.docubot.repository.UseCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UseCaseService {
    private final UseCaseRepository useCaseRepository;
    private final UseCaseHistoryRepository useCaseHistoryRepository;
    private final UseCaseMapper useCaseMapper;

    public UseCase save(UseCase useCase){
        return useCaseRepository.save(useCase);
    }

    public UseCaseResponseDto findById(Long id){
        UseCase useCase = useCaseRepository.findById(id)
                .orElseThrow(()->new RecourseNotFound("El Caso de uso no fue encontrado"));
        return useCaseMapper.toDto(useCase);
    }

    public List<UseCaseResponseDto> findAll(){
        List<UseCase> useCases = useCaseRepository.findAll();
        return useCases
                .stream()
                .map(useCaseMapper::toDto)
                .toList();
    }

    public UseCase getEntityById(Long id) {
        return useCaseRepository.findById(id)
                .orElseThrow(() -> new RecourseNotFound("..."));
    }

    public String extractMessagesByUseCase(Long historyId){
        UseCaseHistory history  = useCaseHistoryRepository.findById(historyId)
                .orElseThrow(()->new RuntimeException("El historial de mensajes no fue encontrado"));

        return history.getMessages();
    }

    public UseCaseResponseDto update(Long id, UseCaseRequestDto useCaseRequestDto){
        UseCase ExistinguUeCase = useCaseRepository.findById(id)
                .orElseThrow(() -> new RecourseNotFound("Caso de uso no encontrado"));
        ExistinguUeCase.setActor(useCaseRequestDto.getActor());
        ExistinguUeCase.setPrecondition(useCaseRequestDto.getPrecondition());
        ExistinguUeCase.setTrigger(useCaseRequestDto.getTrigger());
        ExistinguUeCase.setMain_flow(useCaseRequestDto.getMain_flow());
        ExistinguUeCase.setPostcondition(useCaseRequestDto.getPostcondition());
        return useCaseMapper.toDto(useCaseRepository.save(ExistinguUeCase));
    }

    public void deleteById(Long id){
        useCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Caso de uso no encontrado"));
        useCaseRepository.deleteById(id);
    }

}
