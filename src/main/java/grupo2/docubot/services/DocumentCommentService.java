package grupo2.docubot.services;

import grupo2.docubot.dto.request.DocumentCommentRequestDto;
import grupo2.docubot.dto.response.DocumentCommentResponseDto;
import grupo2.docubot.exceptions.response.ResourceNotFound;
import grupo2.docubot.mappers.DocumentCommentMapper;
import grupo2.docubot.models.Document;
import grupo2.docubot.models.DocumentComment;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.DocumentCommentRepository;
import grupo2.docubot.repository.DocumentRepository;
import grupo2.docubot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentCommentService {

    private final DocumentCommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentCommentMapper commentMapper;

    public List<DocumentCommentResponseDto> getComments(Long documentId) {
        return commentRepository.findByDocumentIdOrderByCreatedAtAsc(documentId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public DocumentCommentResponseDto addComment(Long documentId, DocumentCommentRequestDto dto, Long userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFound("Documento no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("Usuario no encontrado"));

        DocumentComment comment = commentMapper.toEntity(dto);
        comment.setDocument(document);
        comment.setUser(user);

        DocumentComment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }
}
