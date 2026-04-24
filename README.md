# Docubot

Docubot es una herramienta de gestión para analistas funcionales que facilita la confección de documentos de Especificación de Requerimientos de Software (ERS o SRS por sus siglas en inglés).

Es el trabajo final de la materia Programación III de la Tecnicatura Universitaria en Programación de la Universidad Tecnológica Nacional, sede Mar del Plata.

Caso de uso: documentar un sistema de software complejo y preexistente, que carece de documentación previa y de una fuente única y fiable de verdad a la que acudir.

1. Docubot recopila las grabaciones de reuniones y los mensajes de chats corporativos mantenidos entre el analista funcional y los empleados usuarios del sistema en los que haya "textos significativos" (ver definición).

2. Para hacer esa recopilación, el analista funcional marca en las aplicaciones de mensajería o de toma de notas los textos y los mensajes que deben ser recopilados por Docubot.

3. Docubot los almacena en texto plano en la base de datos, y se lo envía a un modelo para limpiar de ruido los textos (pre-procesamiento).

4. Luego envía esos textos pre procesados a un modelo de clasificación (regresión logística) etiqueta los textos significativos según "áreas de trabajo". Devuelve un JSON con una estructura pre definida.

5. Ese material recopilado, curado y etiquetado se almacena en una base de datos y se muestra en una vista para ser estudiada por el analista y, en su caso, agrupar mensajes, descripciones, afirmaciones, según Casos de Uso.

6. Cuando el analista considera que están presentes todos los Casos de Uso que completan o en gran medida satisfacen un Requerimiento, envía ese material desde la vista a un modelo de modelo generativo (LLM) para que genere el ERS.

7. El ERS se muestra en otra vista a la que pueden acceder con permisos de sólo lectura todos los empleados usuarios del sistema documentado (en particular los desarrolladores) para estudiar, hacer comentarios, proponer mejoras, y consultar cuando lo requieran.

Definiciones:

## Textos significativos
Mensajes o fragmentos de reunión que describen el funcionamiento de algún flujo de trabajo dentro de la empresa que tenga relación con el sistema que se quiere documentar

## Áreas de trabajo
Secciones del sistema que atañen a distintas partes de la operatoria de la empresa. Ej.: área de finanzas, área de marketing, área de logística, área de IT.