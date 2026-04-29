package grupo2.docubot.services;

import grupo2.docubot.ChatAnalyzer;
import grupo2.docubot.models.AnalysisResponse;
import grupo2.docubot.models.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ChatAnalysisService {

    private final ChatAnalyzer analyzer;

    public ChatAnalysisService(ChatAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public AnalysisResponse procesarDia() {

        //Simulando los mensajes de un dia
        List<Message> mensajesDelDia = List.of(
                new Message("Analista", "Hola, buen día. Para el módulo de logística, ¿cómo arranca el proceso de despacho de pedidos?", LocalDateTime.of(2026, 4, 28, 9, 15)),
                new Message("Experto (Logística)", "Mira, el despachante entra a la sección de \"Pendientes\". Si el pedido ya está pagado, le da al botón \"Generar Hoja de Ruta\". Ahí el sistema le escupe el PDF con las direcciones y el estado del pedido pasa a \"En Viaje\".", LocalDateTime.of(2026, 4, 28, 9, 20)),
                new Message("Analista", "Perfecto. Cambiando de tema, ¿qué pasa si un cliente quiere devolver algo?", LocalDateTime.of(2026, 4, 28, 11, 30)),
                new Message("Experto (Atención)", "Eso es con el módulo de soporte. El agente busca al cliente por DNI, selecciona el producto de la lista de compras y pone el motivo. El sistema tiene que validar que no hayan pasado más de 30 días. Si está todo ok, se genera un cupón de crédito y el stock del producto vuelve a subir.", LocalDateTime.of(2026, 4, 28, 11, 35)),
                new Message("Analista", "Una duda del despacho de la mañana: ¿el despachante tiene que estar asignado a un depósito?", LocalDateTime.of(2026, 4, 28, 16, 45)),
                new Message("Experto (Logística)", "Sí, claro. Si no tiene depósito asignado ni siquiera puede ver los pendientes.", LocalDateTime.of(2026, 4, 28, 16, 50))
        );


        String historialFormateado = mensajesDelDia.stream()
                .map(m -> m.getUser() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));

        // Unimos todos los mensajes en un solo bloque de texto
        String bloqueTexto = String.join("\n", historialFormateado);

        // Llamamos a la IA (ella parseará el JSON a la lista de objetos automáticamente)
        return analyzer.classify(historialFormateado);

    }
}
