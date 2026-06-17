# 🤖 Docubot

> **Trabajo Final - Programación III**  
> Tecnicatura Universitaria en Programación | **Universidad Tecnológica Nacional (UTN) — Sede Mar del Plata**

Docubot es una herramienta de gestión para **Analistas Funcionales** diseñada para automatizar y facilitar la confección de documentos de **Especificación de Requerimientos de Software (ERS / SRS)**, transformando la comunicación informal de los equipos en documentación técnica estructurada.

---

## 🎯 El Problema y Caso de Uso

**Contexto:** Documentar un sistema de software complejo y preexistente que carece de documentación previa y de una fuente única y fiable de verdad a la que acudir.

**Solución:** Docubot actúa como un puente entre las conversaciones cotidianas (reuniones, chats) y los casos de uso formales, centralizando la información dispersa y utilizando Modelos de Lenguaje Grandes (LLMs) para estructurarla de manera inteligente.

---

## ⚙️ ¿Cómo funciona? (Flujo del Sistema)

1. **Captura:** El analista funcional identifica **"textos significativos"** en las aplicaciones de mensajería o toma de notas corporativas y los reenvía a un chat dedicado.
2. **Ingesta:** Un *webhook* escucha ese chat dedicado y procesa los mensajes de forma asrónica (ej. una vez al día), enviándolos a Docubot.
3. **Estructuración (LLM):** Docubot almacena los mensajes en la base de datos y los envía a un modelo generativo (LLM) con un *system message* específico para estructurarlos en **Casos de Uso**.
4. **Refinamiento:** El analista accede a una **vista dividida**:
   * **Izquierda:** El Caso de Uso generado.
   * **Derecha:** Los mensajes originales que lo fundamentan.
   * *El analista puede corregir, completar o abrir hilos de comentarios que se sincronizan de vuelta con el chat del empleado si falta información.*
5. **Generación de ERS:** Cuando los Casos de Uso están maduros, el analista los envía al LLM para compilar el **ERS** final.
6. **Publicación:** El ERS se publica en una vista de **sólo lectura** para que desarrolladores y *stakeholders* puedan consultar, comentar y proponer mejoras.

---

## 📊 Estructura de Datos y Modelos

### Formato de Caso de Uso esperado (JSON)
El sistema instruye a la IA para que devuelva un formato JSON estricto con la siguiente estructura:

```json
{
  "precondition": "Los requisitos previos que se deben cumplir obligatoriamente.",
  "trigger": "La interacción o evento que da inicio al flujo principal.",
  "main_flow": [
    "I. El usuario realiza una acción...",
    "II. El sistema responde..."
  ],
  "poscondition": "El resultado final o estado en el que queda el sistema."
}
