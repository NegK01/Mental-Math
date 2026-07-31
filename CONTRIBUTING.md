# Guía de Contribución

¡Gracias por tu interés en contribuir a **Mental Math**! Este es un proyecto Open Source nativo para Android diseñado para entrenar el cálculo mental con una interfaz moderna, limpia y fluida.

Para mantener la calidad, consistencia y simplicidad del código, te pedimos seguir las siguientes pautas.

---

## ¿Cómo puedo contribuir?

### 1. Reportar Errores o Proponer Funciones (Issues)
* Revisa primero los [Issues existentes](https://github.com/NegK01/Mental-Math/issues) para evitar duplicados.
* Al crear un nuevo Issue, selecciona el formulario adecuado (Bug, Nueva Función o Consulta General).

### 2. Enviar Código (Pull Requests)
* Toda contribución de código se realiza a través de un **Pull Request (PR)** hacia la rama `main`.

---

## Guía de Estilo y Arquitectura

El proyecto sigue una arquitectura **Clean Architecture + MVVM** nativa en Jetpack Compose, sin frameworks pesados de inyección de dependencias.

* **Lenguaje:** 100% Kotlin.
* **Interfaz de Usuario:** Jetpack Compose con Material 3.
* **Design Tokens:** Usa únicamente las constantes de `Tokens.kt` (`Spacing.*`, `Radius.*`, `Opacity.*`, `Motion.*`) en lugar de valores literales (`16.dp`, `8.dp`).
* **Estado de UI:** Usa `StateFlow` y Hoisting de estado para mantener los Composables puros.

---

## Convención de Commits

En este repositorio mantenemos un estilo de mensajes de commit constante, claro y descriptivo en español:

* **Idioma:** Español, en minúsculas.
* **Estructura:** Comienza con verbos reflexivos como `se agrega...`, `se corrige...`, `se ajusta...`, `se refina...`.

### Ejemplos válidos:
- `se ajusta strings de history_total_games en español`
- `se corrige confusion visual en pantalla de resultados`
- `se adoptan tokens Spacing en toda la ui`
- `se agrega enum Operator al dominio`

---

## Flujo de Trabajo (Git Workflow)

1. Haz un **Fork** del repositorio a tu cuenta personal.
2. Crea una rama descriptiva para tu trabajo:
   ```bash
   git checkout -b feature/nombre-de-tu-mejora
   # o para un arreglo de bug:
   git checkout -b fix/descripcion-del-bug
   ```
3. Verifica que el proyecto compile correctamente:
   ```bash
   ./gradlew assembleDebug
   ```
4. Envía tu **Pull Request** detallando los cambios introducidos y completando la plantilla del PR.
