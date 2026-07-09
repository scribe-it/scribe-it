package grupo2.docubot.controller;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.exceptions.response.ResourceNotFoundException;
import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.models.User;
import grupo2.docubot.services.ChatService;
import grupo2.docubot.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name="Chats", description="Gestión de conversaciones entre analistas y expertos")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @Operation(summary = "Crear chat", description = "Crea un nuevo chat asociado a un departamento. Solo accesible para ANALISTA.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Chat creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasRole('ANALISTA')")
    public ResponseEntity<ChatResponseDto> createChat(@RequestBody @Valid ChatRequestDto chatRequestDto){
        ChatResponseDto newChat = chatService.createChat(chatRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newChat);
    }

    @Operation(summary = "Obtener todos los chats")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de todos los chats"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @PreAuthorize("hasRole('ANALISTA')")
    public ResponseEntity<List<ChatResponseDto>> getAllChats() {
        return ResponseEntity.ok().body(chatService.getAll());
    }

//    @Operation(summary = "Agregar usuario al chat")
//    @PatchMapping("/{chatId}/add/{userId}")
//    @PreAuthorize("hasRole('ANALISTA')")
//    public ResponseEntity<List<User>> addUser(@PathVariable Long chatId,@PathVariable Long userId){
//        return ResponseEntity.ok(chatService.addUser(chatId,userId));
//    }

    @Operation(summary = "Obtener chat por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chat encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Chat no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDto> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getById(id));
    }

    @Operation(summary = "Obtener chat por departamento", description = "Devuelve el chat asociado al departamento del usuario autenticado. Usado por EXPERTOS.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chat del departamento encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "No se encontró chat para el departamento del usuario"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/by-department")
    public ResponseEntity<ChatResponseDto> getChatByDepartment(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(chatService.getByDepartment(user.getDepartment()));
    }

    @Operation(summary = "Marcar mensajes como leídos", description = "Marca todos los mensajes no leídos de un chat como leídos. Solo accesible para ANALISTA.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mensajes marcados como leídos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requiere rol ANALISTA"),
        @ApiResponse(responseCode = "404", description = "Chat no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{chatId}/read")
    @PreAuthorize("hasRole('ANALISTA')")
    public ResponseEntity<Void> markChatAsRead(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.markChatMessagesAsRead(chatId));
    }

    @GetMapping("/{chatId}/messages/search")
    public ResponseEntity<Page<MessageResponseDto>> searchMessageByContent(
            @PathVariable Long chatId,
            @RequestParam String content,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {



        Page<MessageResponseDto> response = messageService.findByChatIdAndContent(
                chatId,
                content.trim(),
                pageable
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{chatId}/timestamp/search/{timestamp}")
    public ResponseEntity<Page<MessageResponseDto>> searchMessagesByTimestamp(
            @PathVariable Long chatId,
            @PathVariable LocalDateTime timestamp,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {


        Page<MessageResponseDto> response = messageService.findByChatIdAndTimestamp(
                chatId,
                timestamp,
                pageable
        );

        return ResponseEntity.ok(response);
    }



}
