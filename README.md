# Mental Math

[![Android SDK](https://img.shields.io/badge/API-26%2B%20(Android%208.0%2B)-brightgreen.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-blue.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

**Mental Math** es una aplicación móvil nativa para Android diseñada para entrenar la agilidad mental mediante ejercicios de cálculo aritmético rápido bajo presión. La aplicación está construida desde cero siguiendo prácticas de la arquitectura nativa en Android con **Jetpack Compose** y **Clean Architecture**.

---

## Descarga

[<img src="https://play.google.com/intl/es/badges/static/images/badges/es_badge_web_generic.png" alt="Disponible en Google Play" height="60">](https://play.google.com/store/apps/details?id=com.negk01.mentalmath)
[<img src="https://user-images.githubusercontent.com/663460/26973090-f8fdc986-4d14-11e7-995a-e7c5e79ed925.png" alt="Descargar APK en GitHub" height="60">](https://github.com/NegK01/Mental-Math/releases)

---

## Características Principales

- **Rondas Rápidas de Cálculo:** Desafíos matemáticos dinámicos con retroalimentación instantánea.
- **3 Niveles de Dificultad:** Fácil, Medio y Difícil (con operaciones combinadas, jerarquía matemática y validación de resultados positivos exactos).
- **Estadísticas e Historial:** Registro local de partidas completadas, tiempos promedio de respuesta y rachas de aciertos (*Best Streak*).
- **Insights por Operador:** Análisis de rendimiento enfocado en suma, resta, multiplicación y división.
- **Sistema de Temas Dinámicos:** Múltiples temas visuales nativos incluyendo *Light*, *Dark*, *OLED*, *Teal Profundo*, *Nordic Frost* y *Royal Dark*.
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

### Tecnologías Clave:
* **Lenguaje:** Kotlin 100% Nativo.
* **UI Framework:** Jetpack Compose con componentes Material 3.
* **Design System:** Sistema de tokens de diseño centralizado (`Tokens.kt`) para `Spacing`, `Radius`, `Opacity` y `Motion`.
* **Persistencia Local:** Room Database v3 con migraciones declaradas.
* **Concurrencia:** Kotlin Coroutines & `StateFlow` / `SharedFlow`.
* **Navegación:** Navigation Compose con estado hoisteado.

---

## Requisitos de Compilación

Para compilar el proyecto localmente necesitas:
- **Android Studio** (Ladybug o superior recomendado).
- **JDK:** Java 17 o Java 21.
- **Android SDK:** `compileSdk 37`, `minSdk 26`.
- **Gradle:** Configurado mediante el Gradle Wrapper incluido (`./gradlew`).

### Pasos para ejecutar localmente:
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

Este proyecto se distribuye bajo la licencia **GNU General Public License v3.0 (GPLv3)**. Consulta el archivo [LICENSE](LICENSE) para más detalles.