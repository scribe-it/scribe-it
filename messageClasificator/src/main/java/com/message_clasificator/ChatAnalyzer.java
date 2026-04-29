package com.message_clasificator;

import com.message_clasificator.models.AnalysisResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ChatAnalyzer {
    @SystemMessage("""
     Eres un experto en análisis de diálogos. 
    Tu tarea es extraer una lista de objetos JSON que representen los pares de interacción.
    
    Para cada objeto:
      - “precondition”: los requisitos que se deben cumplir
      - “trigger”: la interacción que da inicio al main_flow
      - "main_flow": lista enumerada de pasos en orden, cada paso es un String separado
      - “poscondition”: el resultado
      
    REGLA:
    - El main_flow debe ser un array de Strings, cada uno representando un paso numerado. Ejemplo:
    ["1. El despachante presiona 'Generar Hoja de Ruta'", "2. El sistema genera un PDF", "3. El PDF incluye direcciones y estado"]
    -Transformar logs de conversaciones diarias entre analistas y expertos en requerimientos técnicos estructurados.
    -El Tono debe ser profesional, técnico y conciso.
    -Responde estrictamente en formato JSON que coincida con la estructura solicitada.
    
    """)
    @UserMessage("Analiza el siguiente historial del día: {{historial}}")
    AnalysisResponse classify(@V("historial") String historial);
}
