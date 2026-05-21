Fecha: 21/05/2026

Titulo: ¿Debemos modelar la entidad "Departamento"?

Contexto: En principio la aplicación se pensó para que cada chat esté asociada a un departamento.
La relación es uno a uno. Pero después surgió la duda: ¿Y si cada departamento puede tener
más de un chat?

Decisión: Escapa a los requerimientos definidos en el ERS. Preferimos enfocarnos en la solución
más simple, un chat por departamento, y no modelar departamento como una entidad separada. 
Chat tendrá un nombre que será el nombre del departamento, y una descripción que será la
descripción del departamento.
