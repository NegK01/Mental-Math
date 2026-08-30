<div align="center">

# Mental Math

[![Android SDK](https://img.shields.io/badge/API-26%2B%20(Android%208.0%2B)-brightgreen.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-blue.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![License: Source-Available](https://img.shields.io/badge/License-Source--Available-blue.svg)](LICENSE)

🌐 **Languages:** [English](#mental-math) | [Español](#mental-math---español)

<br/>

**Mental Math** is a native Android mobile application designed to train mental agility through fast-paced arithmetic calculations under pressure. The app is built from scratch following native Android architecture best practices using **Jetpack Compose** and **Clean Architecture**.

<br/>

<a href="https://play.google.com/store/apps/details?id=com.negk01.mentalmath">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="60">
</a>
<a href="https://github.com/NegK01/Mental-Math/releases">
  <img src="https://user-images.githubusercontent.com/663460/26973090-f8fdc986-4d14-11e7-995a-e7c5e79ed925.png" alt="Download APK on GitHub" height="60">
</a>

</div>

<hr/>

## Key Features

- **Fast Calculation Rounds:** Dynamic math challenges with instant feedback.
- **3 Difficulty Levels:** Easy, Medium, and Hard (with combined operations, mathematical precedence, and positive integer validation).
- **Statistics & History:** Local records of completed games and average response times. Includes a dedicated **Personal Bests** section tracking top scores by difficulty.
- **Daily Streak Calendar:** An interactive visual calendar tracking your daily activity directly on the home screen.
- **Operator Insights:** Performance analysis focused on addition, subtraction, multiplication, and division.
- **Dynamic Theme System:** Multiple native visual themes including *Light*, *Dark*, *OLED*, *Deep Teal*, *Nordic Frost*, *Royal Dark*, and *Graphite Lime*.
- **Native Multilingual Support:** Full localization in Spanish and English with dynamic language switching.
- **Immersive Edge-to-Edge Experience:** Clean, distraction-free interface taking full advantage of the device screen.

---

## Architecture & Tech Stack

The project is designed under **Clean Architecture + MVVM** principles, prioritizing native performance, component decoupling, and modularity without over-engineering.

```text
mentalmath
├─ data          # Room implementations (DAO, Entities), Mappers, and Repositories
├─ domain        # Pure domain models (Question, GameRecord) and Business Logic (QuestionGenerator)
├─ presentation  # ViewModels and UI States (StateFlow, UI State)
├─ ui            # Compose Components, Screens, and Design System (Design Tokens)
└─ navigation    # Centralized navigation with Navigation Compose
```

### Key Technologies

| Category | Technology / Specification |
| :--- | :--- |
| **Language** | 100% Native Kotlin |
| **UI Framework** | Jetpack Compose with Material 3 components |
| **Design System** | Centralized design tokens (`Tokens.kt`) for `Spacing`, `Radius`, `Opacity`, and `Motion` |
| **Local Persistence** | Room Database with declared migrations |
| **Concurrency** | Kotlin Coroutines & `StateFlow` / `SharedFlow` |
| **Navigation** | Navigation Compose with hoisted state |

---

## Build Requirements

To build the project locally you need:
- **Android Studio** (Panda or newer recommended).
- **JDK:** Java 17 or Java 21.
- **Android SDK:** `compileSdk 37`, `minSdk 26`.
- **Gradle:** Configured via the included Gradle Wrapper (`./gradlew`).

### Steps to run locally

1. Clone the repository:
   ```bash
   git clone https://github.com/NegK01/Mental-Math.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and run the app on an emulator or physical device with Android 8.0 (API 26) or higher.

```bash
./gradlew assembleDebug
```

---

## Contributing

Contributions are welcome! If you wish to report a bug, propose an enhancement, or submit code, please review our [Contribution Guide](CONTRIBUTING.md).

---

## License

This project is source-available. All rights are reserved by the author, with community contributions welcomed via Issues and Pull Requests. See the [LICENSE](LICENSE) file for details.

---
---

<div align="center">

# Mental Math - Español

[![Android SDK](https://img.shields.io/badge/API-26%2B%20(Android%208.0%2B)-brightgreen.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-blue.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Licencia: Source-Available](https://img.shields.io/badge/Licencia-Source--Available-blue.svg)](LICENSE)

🌐 **Idiomas:** [English](#mental-math) | [Español](#mental-math---español)

<br/>

**Mental Math** es una aplicación móvil nativa para Android diseñada para entrenar la agilidad mental mediante ejercicios de cálculo aritmético rápido bajo presión. La aplicación está construida desde cero siguiendo prácticas de la arquitectura nativa en Android con **Jetpack Compose** y **Clean Architecture**.

<br/>

<a href="https://play.google.com/store/apps/details?id=com.negk01.mentalmath">
  <img src="https://play.google.com/intl/es/badges/static/images/badges/es_badge_web_generic.png" alt="Disponible en Google Play" height="60">
</a>
<a href="https://github.com/NegK01/Mental-Math/releases">
  <img src="https://user-images.githubusercontent.com/663460/26973090-f8fdc986-4d14-11e7-995a-e7c5e79ed925.png" alt="Descargar APK en GitHub" height="60">
</a>

</div>

<hr/>

## Características Principales

- **Rondas Rápidas de Cálculo:** Desafíos matemáticos dinámicos con retroalimentación instantánea.
- **3 Niveles de Dificultad:** Fácil, Medio y Difícil (con operaciones combinadas, jerarquía matemática y validación de resultados positivos exactos).
- **Estadísticas e Historial:** Registro local de partidas completadas y tiempos promedio de respuesta. Incluye una sección dedicada a **Mejores Marcas Personales** por dificultad.
- **Calendario de Racha Diaria:** Un calendario visual interactivo que rastrea tu actividad diaria directamente en la pantalla de inicio.
- **Insights por Operador:** Análisis de rendimiento enfocado en suma, resta, multiplicación y división.
- **Sistema de Temas Dinámicos:** Múltiples temas visuales nativos incluyendo *Light*, *Dark*, *OLED*, *Teal Profundo*, *Nordic Frost*, *Royal Dark* y *Graphite Lime*.
- **Soporte Multilingüe Nativo:** Localización completa en Español e Inglés con cambio dinámico de idioma.
- **Experiencia Inmersiva Edge-to-Edge:** Interfaz limpia sin distracciones que aprovecha la pantalla completa del dispositivo.

---

## Arquitectura y Stack Tecnológico

El proyecto está diseñado bajo los principios de **Clean Architecture + MVVM**, priorizando el rendimiento nativo, el desacoplamiento de componentes y la modularidad sin sobreingeniería.

```text
mentalmath
├─ data          # Implementaciones de Room (DAO, Entities), Mappers y Repositorios
├─ domain        # Modelos de dominio puros (Question, GameRecord) y Lógica de Negocio (QuestionGenerator)
├─ presentation  # ViewModels y Estados de UI (StateFlow, UI State)
├─ ui            # Componentes Compose, Pantallas y Sistema de Diseño (Design Tokens)
└─ navigation    # Navegación centralizada con Navigation Compose
```

### Tecnologías Clave

| Categoría | Tecnología / Especificación |
| :--- | :--- |
| **Lenguaje** | 100% Kotlin Nativo |
| **UI Framework** | Jetpack Compose con componentes Material 3 |
| **Design System** | Sistema de tokens de diseño centralizado (`Tokens.kt`) para `Spacing`, `Radius`, `Opacity` y `Motion` |
| **Persistencia Local** | Room Database con migraciones declaradas |
| **Concurrencia** | Kotlin Coroutines & `StateFlow` / `SharedFlow` |
| **Navegación** | Navigation Compose con estado hoisteado |

---

## Requisitos de Compilación

Para compilar el proyecto localmente necesitas:
- **Android Studio** (Panda o superior recomendado).
- **JDK:** Java 17 o Java 21.
- **Android SDK:** `compileSdk 37`, `minSdk 26`.
- **Gradle:** Configurado mediante el Gradle Wrapper incluido (`./gradlew`).

### Pasos para ejecutar localmente

1. Clona el repositorio:
   ```bash
   git clone https://github.com/NegK01/Mental-Math.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle y ejecuta la app en un emulador o dispositivo físico con Android 8.0 (API 26) o superior.

```bash
./gradlew assembleDebug
```

---

## Contribución

¡Las contribuciones son bienvenidas! Si deseas reportar un error, proponer una mejora o enviar código, por favor revisa nuestra [Guía de Contribución](CONTRIBUTING.md).

---

## Licencia

Este proyecto es de **código fuente disponible** (*source-available*). Todos los derechos están reservados por el autor, permitiendo contribuciones de la comunidad mediante Issues y Pull Requests. Consulta el archivo [LICENSE](LICENSE) para más detalles.