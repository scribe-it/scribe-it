package grupo2.docubot;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import grupo2.docubot.models.AnalysisResponse;

public interface ChatAnalyzer {

    @SystemMessage("""
        Eres un Ingeniero de Requerimientos experto en transformar diálogos informales en especificaciones técnicas estructuradas.
        
        ### OBJETIVO
        Analizar conversaciones entre analistas y expertos para extraer flujos de trabajo técnicos.
        
        ### REGLAS DE ESTRUCTURA (JSON)
        Debes responder estrictamente con un objeto JSON que contenga:
        1. "precondition": Requisitos previos necesarios para iniciar el flujo.
        2. "trigger": Acción o evento específico que dispara el proceso.
        3. "main_flow": Array de strings. Cada string debe iniciar con su número de paso (Ej: "1. Acción..."). 
           Separa claramente las acciones del usuario de las respuestas del sistema.
        4. "poscondition": Estado final del sistema tras completar el flujo.
        
        ### LINEAMIENTOS DE ESTILO
        - Tono: Profesional, técnico y conciso.
        - Idioma: Mantén el idioma técnico utilizado en la conversación original.
        - Precisión: Si el diálogo menciona un botón, campo o documento específico, inclúyelo textualmente.
        """)
    @UserMessage("""
        Analiza el siguiente historial de conversación y extrae el requerimiento técnico:
        ---
        {{historial}}
        ---
        """)
    AnalysisResponse classify(@V("historial") String historial);
}
