# Docubot

Docubot es una herramienta de gestión para analistas funcionales que facilita la confección de documentos de Especificación de Requerimientos de Software (ERS o SRS por sus siglas en inglés).

Es el trabajo final de la materia Programación III de la Tecnicatura Universitaria en Programación de la Universidad Tecnológica Nacional, sede Mar del Plata.

Caso de uso: documentar un sistema de software complejo y preexistente, que carece de documentación previa y de una fuente única y fiable de verdad a la que acudir.

1. Docubot recopila las grabaciones de reuniones y los mensajes de chats corporativos mantenidos entre el analista funcional y los empleados usuarios del sistema en los que haya "textos significativos" (ver definición).

2. Para hacer esa recopilación, el analista funcional marca en las aplicaciones de mensajería o de toma de notas los textos los mensajes que deben ser recopilados por Docubot.

3. El analista reenvía a un chat dedicado los textos significativos.

4. Un webhook escucha ese chat y envía todos los mensajes 1 vez al dia al Docubot.

5. Docubot los guarda en la base de datos, y al mismo tiempo se los envía a un modelo generativo (LLM) para que estructure el conjunto de mensajes en uno o varios casos de uso según la siguiente estructura:
  
Caso de uso

	Precondición: El empleado tiene permisos de administrador y la base de datos está disponible.

	Disparador: El empleado apreta el botón “Registrarme”.

	Flujo principal:
    I.  El empleado ingresa a la vista de registro.
		II. El sistema muestra el formulario.
		III. El empleado completa el formulario.
		IV. El empleado hace click en “Guardar”.
		V. El sistema envía un correo de confirmación.
		VI. El empleado abre el correo y confirma.
		
	Poscondición: El nuevo usuario se guarda en la base de datos con un número de legajo generado automáticamente.

En el system message enviado a la IA, se pedirá un JSON con esas mismas propiedades:

“precondition”: los requisitos que se deben cumplir
“trigger”: la interacción que da inicio al main_flow
“main_flow”: el conjunto de interacciones ppales
“poscondition”: el resultado

6. Una vez generado el o los Casos de Uso, se almacenan en la base de datos y se muestran en una vista dividida al analista funcional.

7. El analista evalúa en esa vista dividida que tiene de un lado el Caso de Uso y del otro los mensajes que lo originaron, corrige lo que se haya malinterpreado,  completa lo que falte, y en su caso, si le falta información, abre un comentario en ese mismo Caso de Uso cuyo contenido (la duda del analista) se envía como mensaje a la aplicación de mensajería que tenga con el empleado.

8. Cuando el analista considera que están presentes todos los Casos de Uso que completan o en gran medida satisfacen un Requerimiento, envía ese material desde la vista a un modelo de modelo generativo (LLM) para que genere el ERS.

9. El ERS se muestra en otra vista a la que pueden acceder con permisos de sólo lectura todos los empleados usuarios del sistema documentado (en particular los desarrolladores) para estudiar, hacer comentarios, proponer mejoras, y consultar cuando lo requieran.

DER:
![clase uml](/Clase UML.png)

<!---
5. Docubot los almacena en texto plano en la base de datos, y se lo envía a un modelo para limpiar de ruido los textos (pre-procesamiento).

6. Luego envía esos textos pre procesados a un modelo de clasificación (regresión logística) etiqueta los textos significativos según "áreas de trabajo". Devuelve un JSON con una estructura pre definida.

7. Ese material recopilado, curado y etiquetado se almacena en una base de datos y se muestra en una vista para ser estudiada por el analista y, en su caso, agrupar mensajes, descripciones, afirmaciones, según Casos de Uso.
--->

Definiciones:

## Textos significativos
Mensajes o fragmentos de reunión que describen el funcionamiento de algún flujo de trabajo dentro de la empresa que tenga relación con el sistema que se quiere documentar

## Áreas de trabajo
Secciones del sistema que atañen a distintas partes de la operatoria de la empresa. Ej.: área de finanzas, área de marketing, área de logística, área de IT.

## ERS
Especificación de Requerimientos de Software.
