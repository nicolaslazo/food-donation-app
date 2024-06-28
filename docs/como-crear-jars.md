*Guía de creación de jars*

El propósito de esta guía va a ser crear un jar individual y autocontenido (tiene otras dependencias). Probablemente no sea escalable al punto de poder convertir todo el proyecto en un solo jar, pero es un principio. Lo escribo en inglés porque tengo el IDE así pero las palabras no deberían cambiar demasiado.

1. Navegar los siguientes menúes: `File` -> `Project Structure`
2. En la sección de `Project Settings`: `Artifacts` -> `+` (el signo de más) -> `Jar` -> `From modules with dependencies...`
3. En `Main class` apretan el ícono de la carpetita y van a la tab del proyecto
4. Seleccionan el archivo con el main que quieren convertir a un jar
5. Seleccionan `Extract to the target jar` y `OK`
6. Van a tener un artefacto nuevo. Le ponen el nombre que quieran en el campo de nombre y apretan `OK`
7. Deberían estar de vuelta en el IDE con todos los settings cerrados
8. Van a los siguientes menúes: `Build` -> `Build artifacts...`
9. Seleccionan el artefacto y apretan `Build`

El jar va a estar en el directorio `out` en la raíz del proyecto, le dan click derecho, run y debería aparecerles una terminal con el output