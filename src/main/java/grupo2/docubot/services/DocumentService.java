package grupo2.docubot.services;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.exceptions.response.ResourceNotFound;
import grupo2.docubot.mappers.DocumentMapper;
import grupo2.docubot.models.Document;
import grupo2.docubot.models.UseCase;
import grupo2.docubot.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final UseCaseService useCaseService;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public List<DocumentResponseDto> getAll() {
        List<Document> documents = documentRepository.findAll();

        return documents.stream()
                .map(documentMapper::toDto)
                .toList();
    }

    public DocumentResponseDto create(DocumentRequestDto documentRequestDto) {
        Document newDocument = Document.builder().title(documentRequestDto.getTitle()).build();
        Document savedDocument = documentRepository.save(newDocument);
        if(documentRequestDto.getContent() != null ){
            documentRequestDto.getContent().forEach(uc -> {
                UseCase useCase = useCaseService.getEntityById(uc.getId());
                useCase.setDocument(savedDocument);
                useCaseService.save(useCase);
            });
        }
        return documentMapper.toDto(documentRepository.findById(savedDocument.getId())
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"))
        );
    }

    public DocumentResponseDto addUseCase(Long documentId, UseCase useCase) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));
        document.getContent().add(useCase);
        Document documentUpdated = documentRepository.save(document);
        useCase.setDocument(document);
        useCaseService.save(useCase);
        return documentMapper.toDto(documentUpdated);
    }
}
