package grupo2.docubot.services;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.exceptions.response.NonPublishedDocumentException;
import grupo2.docubot.exceptions.response.ResourceNotFound;
import grupo2.docubot.exceptions.response.UnsafeToDeleteException;
import grupo2.docubot.mappers.DocumentMapper;
import grupo2.docubot.mappers.UseCaseMapper;
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
    private final UseCaseMapper useCaseMapper;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public List<DocumentResponseDto> getAllDrafts() {
        List<Document> documents = documentRepository.findAllByPublishedFalse();

        return documents.stream()
                .map(documentMapper::toDto)
                .toList();
    }

    public DocumentResponseDto create(DocumentRequestDto documentRequestDto) {
        Document newDocument = Document.builder().title(documentRequestDto.getTitle()).build();
        if(documentRequestDto.getContent() != null ){
            documentRequestDto.getContent().forEach(uc -> {
                UseCase useCase = useCaseService.getEntityById(uc.getId());
                useCase = useCaseService.save(useCase);
                newDocument.getContent().add(useCase);
            });
        }
        Document savedDocument = documentRepository.save(newDocument);
        return documentMapper.toDto(documentRepository.findById(savedDocument.getId())
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"))
        );
    }

    public DocumentResponseDto addUseCase(Long documentId, UseCaseRequestDto useCaseRequestDto) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));
        UseCase useCase = useCaseMapper.toEntity(useCaseRequestDto);
        useCase = useCaseService.save(useCase);
        document.getContent().add(useCase);
        Document documentUpdated = documentRepository.save(document);
        return documentMapper.toDto(documentUpdated);
    }

    public void removeUseCase(Long documentId, Long useCaseId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));
        document.getContent().removeIf(uc -> uc.getId().equals(useCaseId));
        documentRepository.save(document);
    }

    public DocumentResponseDto publishDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));
        document.setPublished(true);
        documentRepository.save(document);
        return documentMapper.toDto(document);
    }

    public DocumentResponseDto unpublishDocument(Long documentId){
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));
        if(document.getPublished() == true){
            document.setPublished(false);
            documentRepository.save(document);
        }
        else{
            throw new NonPublishedDocumentException("El documento no fue publicado");
        }
        return documentMapper.toDto(document);
    }

    public List<DocumentResponseDto> getAllPublished() {
        return documentRepository.findAllByPublishedTrue()
                .stream()
                .map(documentMapper::toDto)
                .toList();
    }

    public void deleteDraft(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Document not found"));
        if(document.getPublished()) {
            throw new UnsafeToDeleteException("The document is published: cannot hard delete");
        }
        documentRepository.delete(document);
    }
}
