CONTEXTO

Proyecto: Mental Math (Android, Kotlin, Jetpack Compose, Room)

Descripción general
Mental Math es una aplicación móvil para entrenar cálculo mental mediante rondas rápidas con límite de tiempo. El usuario resuelve operaciones aritméticas bajo presión, con tres niveles de dificultad. La app guarda estadísticas de partidas completas y permite ver progreso mediante historial y métricas.

Arquitectura
La app sigue una separación por capas clara:

data
Contiene Room (database, dao, entities), mappers y repositories (implementaciones).

domain
Contiene modelos puros (GameRecord, Difficulty, Question, etc.) y lógica de negocio como QuestionGenerator y configuraciones de juego.

presentation
Contiene ViewModels por pantalla (Home, Config, Game, History). Manejan estado y lógica de UI.

ui
Contiene composables (pantallas y componentes reutilizables) y theme.

navigation
Contiene AppNavigation con NavHost y rutas.

Tecnologías usadas
Kotlin
Jetpack Compose (Material 3)
Room (persistencia local)
StateFlow
ViewModel
KSP con Version Catalog

Funcionalidades actuales

Juego
Tres dificultades: fácil, medio y difícil
Tiempo por ronda (no por partida)
Rondas definidas por dificultad
Input manual con teclado numérico
Botón borrar (C) elimina último dígito
Botón pausa con modal
Botón salir (abandono)

Lógica del juego
Fácil: operaciones simples con dos números
Medio: operaciones combinadas sin negativos
Difícil: operaciones con prioridad operatoria y paréntesis
División siempre exacta (sin decimales)
Nunca divisor cero
Resultados negativos solo en difícil
Si el tiempo se acaba:
Se envía automáticamente la respuesta actual
Si está vacío se interpreta como 0
Se pasa a la siguiente ronda

Resultados
Pantalla de resultados solo para la sesión actual (no persistente)
Muestra:
aciertos / total
tiempo promedio
detalle por ronda
estado: completada o abandonada

Persistencia (Room)

Se guarda únicamente:
partidas completadas

No se guardan:
partidas abandonadas
partidas sin progreso

Tabla game_records:
id
playedAt
difficulty (string)
correctAnswers
totalRounds
averageResponseTimeMillis
maxStreak

Tabla settings:
id único
selectedDifficulty
soundEnabled

Pantallas

Home
Muestra últimas 3 partidas
Muestra racha diaria calculada dinámicamente
Botón iniciar juego

Game
Pantalla principal del juego
Controla rondas, tiempo, input

Results
Feedback inmediato de la partida
No accesible desde navegación directa
Solo flujo Game → Results

History
Lista completa de partidas guardadas
Summary arriba con:
total partidas
precisión promedio
tiempo promedio
mejor racha

Config
Cambiar dificultad
Activar/desactivar sonido
Borrar historial completo

Lógica importante implementada

La generación de preguntas fue reescrita para evitar inconsistencias entre expresión y resultado.
Ahora todas las expresiones y respuestas se generan desde la misma lógica, sin combinar resultados parciales de otras preguntas.

Racha diaria
Se calcula a partir de fechas únicas de partidas
Cuenta días consecutivos (hoy o ayer hacia atrás)

Repositorios
SettingsRepository
GameRecordRepository

ViewModels usan factories para inyección manual (sin Hilt)

Estado actual del proyecto
App completamente funcional
Persistencia funcionando
Sin crashes conocidos
Generación de preguntas corregida
UX base sólida
Lista para publicación en Play Store

Decisiones de diseño importantes

No se guarda detalle por ronda en base de datos (solo en memoria para Results)
Resultados no forman parte del historial persistente
Historial es solo partidas completas
UI simple priorizando claridad sobre complejidad
Sin sobreingeniería (sin DI, sin use cases, sin modularización avanzada aún)

Futuros cambios (prioridad recomendada)

Nivel 1 (mejoras inmediatas)
Confirmación al borrar historial
Mover textos a strings.xml (internacionalización)
Pequeños ajustes de UI/UX (espaciado, jerarquía visual)

Nivel 2 (mejoras de producto)
Modo oscuro (solo cambiar theme)
Mejor diseño del Summary en History (tipo dashboard)
Mejor feedback visual en Game (animaciones, colores, estados)

Nivel 3 (features nuevas)
Filtros en historial (semana, mes)
Gráficos de progreso
Racha diaria persistente (no recalculada cada vez)
Sonidos / feedback háptico
Sistema de logros

Nivel 4 (arquitectura avanzada)
Migrar a Clean Architecture más estricta (use cases)
Implementar Hilt (inyección de dependencias)
Tests unitarios
Separación de módulos

Notas importantes

El generador de preguntas fue el bug crítico más importante y ya está solucionado.
La app está en un punto ideal para publicación y validación con usuarios reales.
No conviene añadir más features antes de recibir feedback.

Estado final

Proyecto completo, estable, funcional y listo para distribución.



```
mentalmath
├─ data
│  ├─ local
│  │  ├─ dao
│  │  │  ├─ GameRecordDao.kt
│  │  │  └─ SettingsDao.kt
│  │  ├─ db
│  │  │  ├─ AppDatabase.kt
│  │  │  └─ DatabaseProvider.kt
│  │  └─ entity
│  │     ├─ GameRecordEntity.kt
│  │     └─ SettingsEntity.kt
│  ├─ mapper
│  │  ├─ GameRecordMapper.kt
│  │  └─ SettingsMapper.kt
│  └─ repository
│     ├─ GameRecordRepositoryImpl.kt
│     └─ SettingsRepositoryImpl.kt
├─ domain
│  ├─ game
│  │  └─ QuestionGenerator.kt
│  ├─ model
│  │  ├─ AppSettings.kt
│  │  ├─ Difficulty.kt
│  │  ├─ GameConfig.kt
│  │  ├─ GameRecord.kt
│  │  ├─ GameSessionResult.kt
│  │  ├─ Question.kt
│  │  ├─ RoundDetail.kt
│  │  ├─ RoundResult.kt
│  │  └─ Score.kt
│  └─ repository
│     ├─ GameRecordRepository.kt
│     └─ SettingsRepository.kt
├─ MainActivity.kt
├─ navigation
│  ├─ AppNavigation.kt
│  └─ Routes.kt
├─ presentation
│  ├─ config
│  │  ├─ ConfigUiState.kt
│  │  ├─ ConfigViewModel.kt
│  │  └─ ConfigViewModelFactory.kt
│  ├─ game
│  │  ├─ GameUiState.kt
│  │  ├─ GameViewModel.kt
│  │  └─ GameViewModelFactory.kt
│  ├─ history
│  │  ├─ HistoryUiState.kt
│  │  ├─ HistoryViewModel.kt
│  │  └─ HistoryViewModelFactory.kt
│  ├─ home
│  │  ├─ HomeUiState.kt
│  │  ├─ HomeViewModel.kt
│  │  └─ HomeViewModelFactory.kt
│  └─ results
│     ├─ ResultsUiState.kt
│     └─ ResultsViewModel.kt
└─ ui
   ├─ components
   │  └─ BottomNavBar.kt
   ├─ screens
   │  ├─ config
   │  │  ├─ components
   │  │  │  ├─ DangerZone.kt
   │  │  │  ├─ DifficultyItem.kt
   │  │  │  ├─ DifficultySelector.kt
   │  │  │  └─ OptionSwitch.kt
   │  │  └─ ConfigScreen.kt
   │  ├─ game
   │  │  ├─ components
   │  │  │  ├─ AnswerDisplay.kt
   │  │  │  ├─ GameProgressCard.kt
   │  │  │  ├─ GameTopBar.kt
   │  │  │  ├─ MathQuestionCard.kt
   │  │  │  ├─ NumberPad.kt
   │  │  │  └─ PauseDialog.kt
   │  │  └─ GameScreen.kt
   │  ├─ history
   │  │  ├─ components
   │  │  │  └─ HistorySummaryCard.kt
   │  │  └─ HistoryScreen.kt
   │  ├─ home
   │  │  ├─ components
   │  │  │  ├─ DailyStreakCard.kt
   │  │  │  ├─ HomeHeader.kt
   │  │  │  ├─ RecentScoreItem.kt
   │  │  │  ├─ RecentScoresCard.kt
   │  │  │  └─ StartGameButton.kt
   │  │  └─ HomeScreen.kt
   │  └─ results
   │     ├─ components
   │     │  ├─ CompletionStatusBadge.kt
   │     │  ├─ ResultsActions.kt
   │     │  ├─ ResultsSummaryCard.kt
   │     │  ├─ ResultStatCard.kt
   │     │  ├─ RoundDetailItem.kt
   │     │  └─ RoundDetailsCard.kt
   │     └─ ResultsScreen.kt
   ├─ theme
   │  ├─ Color.kt
   │  ├─ Theme.kt
   │  └─ Type.kt
   └─ utils
      ├─ CompletionStatusUi.kt
      └─ DifficultyUi.kt

```


ACTUALIZACION DEL CONTEXTO V.2
Esto debe de leerse despues de leer el texto inicial
El nivel 3 y 4 no se realizaran salvo Filtros en historial (semana, mes) y Racha diaria persistente (no recalculada cada vez) pero no por ahora


CONTEXTO DEL PROYECTO

Proyecto: Mental Math
Stack: Android, Kotlin, Jetpack Compose, Room, ViewModel, StateFlow
Estado de producto: ya fue publicado en Play Store

RESUMEN

Mental Math es una app de calculo mental con rondas rapidas y limite de tiempo.
El usuario juega sesiones cortas con tres niveles de dificultad, ve resultados de la sesion actual y consulta su historial persistente.

ARQUITECTURA

La app mantiene una separacion por capas:

- `data`: Room, entities, dao, mappers y repositories
- `domain`: modelos puros y logica del juego
- `presentation`: ViewModels y UiState por pantalla
- `ui`: pantallas Compose, componentes y theme
- `navigation`: `AppNavigation` y rutas

Persistencia:

- Room con `AppDatabase`
- Version actual de base de datos: `3`
- Se usa `fallbackToDestructiveMigration()`
- Ya se aviso a usuarios que cambios de esquema pueden borrar datos, asi que no se estan haciendo migraciones manuales

ESTADO FUNCIONAL ACTUAL

Juego:

- 3 dificultades: `easy`, `medium`, `hard`
- rondas y tiempo por dificultad
- teclado numerico propio
- boton de pausar
- boton de salir
- resultados de la sesion actual

Persistencia:

- se guardan partidas completadas
- no se guardan partidas abandonadas
- `GameRecord.difficulty` ya usa `Difficulty`
- `Settings` ya guarda:
  - dificultad
  - sonido
  - tema
  - idioma

Configuracion:

- selector de dificultad
- sonido on/off
- selector de tema: `system`, `light`, `dark`
- selector de idioma: `system`, `es`, `en`
- confirmacion para borrar historial

Tema e idioma:

- la app aplica tema global desde `MentalMathTheme`
- idioma por app usando `AppCompatDelegate.setApplicationLocales(...)`
- system bars y dialogs ya fueron trabajados en esta etapa

History:

- summary superior sin `bestStreak`
- solo muestra:
  - total de partidas
  - precision promedio
  - tiempo promedio

Home:

- el usuario rehizo manualmente parte del Home
- no se tomo como definitiva la propuesta anterior de navegacion y header
- el estado visual actual del Home es funcional pero necesita limpieza

DECISIONES IMPORTANTES

- No usar migraciones manuales por ahora.
- No reactivar `bestStreak`.
- El generador de preguntas ya fue corregido y sigue siendo el punto mas importante ya resuelto.
- No meter features grandes sin motivo claro; primero pulir UX y validar con usuarios.

PENDIENTES INMEDIATOS

Estos son los puntos mas cercanos y concretos que quedaron pendientes:

1. Limpiar codigo comentado en Home.
   Archivos visibles con ese problema:
   - `ui/screens/home/components/HomeHeader.kt`
   - `ui/screens/home/components/StartGameButton.kt`

2. Reemplazar "gradientes" de un solo color por color solido.
   Ahora hay bloques donde se dejo estructura de gradiente comentada o innecesaria y debe quedar un `color = ...` simple.

3. Corregir la racha diaria en Home.
   Situacion actual:
   - en `HomeHeader.kt` el numero esta hardcodeado en `"0"`
   - no debe quedarse hardcodeado
   - hay que leer el valor real desde `HomeUiState.dailyStreak` con r.string que se encuentra en C:\Users\Admin\AndroidStudioProjects\MentalMath\app\src\main\res
   - el texto visible que se quiere en esa vista es solo el numero, no la frase completa "dias consecutivos"

4. Ajustar recursos para la racha.
   El plural actual es:

   ```xml
   <plurals name="daily_streak_days">
       <item quantity="one">%d dia consecutivo</item>
       <item quantity="other">%d dias consecutivos</item>
   </plurals>
   ```

   Si en Home solo se quiere mostrar el numero, hay que crear o reutilizar un recurso distinto para eso.
   No conviene seguir usando ese plural en la UI del header si la intencion es mostrar solo el numero.

5. Revisar consistencia del Home.
   Estado actual observable:
   - `HomeHeader.kt` tiene imports y codigo comentado de versiones anteriores
   - `StartGameButton.kt` conserva estructura de gradiente comentada
   - `RecentScoresCard.kt` parece mas limpio, pero debe revisarse junto con el resto del Home para mantener un lenguaje visual coherente

ARCHIVOS CLAVE PARA EL SIGUIENTE CHAT

- `MainActivity.kt`
- `navigation/AppNavigation.kt`
- `data/local/db/AppDatabase.kt`
- `data/local/db/DatabaseProvider.kt`
- `domain/model/AppSettings.kt`
- `domain/model/ThemePreference.kt`
- `domain/model/LanguagePreference.kt`
- `presentation/config/ConfigViewModel.kt`
- `presentation/home/HomeUiState.kt`
- `ui/screens/home/HomeScreen.kt`
- `ui/screens/home/components/HomeHeader.kt`
- `ui/screens/home/components/RecentScoresCard.kt`
- `ui/screens/home/components/StartGameButton.kt`
- `ui/theme/Theme.kt`
- `res/values/strings.xml`

REGLAS DE COLABORACION IMPORTANTES

- Antes de cada compilacion o build, pedir permiso personalmente.
- No correr compilacion por defecto.
- Si hace falta agregar librerias nuevas, confirmarlo primero.
- No volver a agregar el import explicito `import androidx.compose.foundation.layout.weight`.
- No asumir que una propuesta visual anterior sigue vigente si el usuario ya rehizo manualmente la pantalla.

ESTADO REAL DEL HOME AL MOMENTO DE ESTE README

- El Home fue modificado manualmente por el usuario despues de una propuesta anterior.
- Lo que hay ahora no debe reescribirse desde cero sin revisar primero el codigo actual.
- La prioridad no es inventar un nuevo Home, sino limpiar y consolidar el que el usuario ya dejo.

OBJETIVO RECOMENDADO PARA EL SIGUIENTE CHAT

Tomar el Home actual y hacer solo un pase de consolidacion:

- quitar comentarios y restos de versiones anteriores
- dejar colores solidos donde no hace falta gradiente
- conectar la racha real en vez del `0` hardcodeado
- crear el recurso correcto si Home solo debe mostrar el numero de racha
- mantener la direccion visual que el usuario ya eligio

```
mentalmath
├─ data
│  ├─ local
│  │  ├─ dao
│  │  │  ├─ GameRecordDao.kt
│  │  │  └─ SettingsDao.kt
│  │  ├─ db
│  │  │  ├─ AppDatabase.kt
│  │  │  └─ DatabaseProvider.kt
│  │  └─ entity
│  │     ├─ GameRecordEntity.kt
│  │     └─ SettingsEntity.kt
│  ├─ mapper
│  │  ├─ GameRecordMapper.kt
│  │  └─ SettingsMapper.kt
│  └─ repository
│     ├─ GameRecordRepositoryImpl.kt
│     └─ SettingsRepositoryImpl.kt
├─ domain
│  ├─ game
│  │  └─ QuestionGenerator.kt
│  ├─ model
│  │  ├─ AppSettings.kt
│  │  ├─ Difficulty.kt
│  │  ├─ GameConfig.kt
│  │  ├─ GameRecord.kt
│  │  ├─ GameSessionResult.kt
│  │  ├─ LanguagePreference.kt
│  │  ├─ Question.kt
│  │  ├─ RoundDetail.kt
│  │  ├─ RoundResult.kt
│  │  ├─ Score.kt
│  │  └─ ThemePreference.kt
│  └─ repository
│     ├─ GameRecordRepository.kt
│     └─ SettingsRepository.kt
├─ MainActivity.kt
├─ navigation
│  ├─ AppNavigation.kt
│  └─ Routes.kt
├─ presentation
│  ├─ config
│  │  ├─ ConfigUiState.kt
│  │  ├─ ConfigViewModel.kt
│  │  └─ ConfigViewModelFactory.kt
│  ├─ game
│  │  ├─ GameUiState.kt
│  │  ├─ GameViewModel.kt
│  │  └─ GameViewModelFactory.kt
│  ├─ history
│  │  ├─ HistoryUiState.kt
│  │  ├─ HistoryViewModel.kt
│  │  └─ HistoryViewModelFactory.kt
│  ├─ home
│  │  ├─ HomeUiState.kt
│  │  ├─ HomeViewModel.kt
│  │  └─ HomeViewModelFactory.kt
│  └─ results
│     ├─ ResultsUiState.kt
│     └─ ResultsViewModel.kt
├─ README.md
└─ ui
   ├─ components
   │  └─ BottomNavBar.kt
   ├─ screens
   │  ├─ config
   │  │  ├─ components
   │  │  │  ├─ DangerZone.kt
   │  │  │  ├─ DifficultyItem.kt
   │  │  │  ├─ DifficultySelector.kt
   │  │  │  ├─ LanguagePreferenceSelector.kt
   │  │  │  ├─ OptionSwitch.kt
   │  │  │  └─ ThemePreferenceSelector.kt
   │  │  └─ ConfigScreen.kt
   │  ├─ game
   │  │  ├─ components
   │  │  │  ├─ AnswerDisplay.kt
   │  │  │  ├─ GameProgressCard.kt
   │  │  │  ├─ GameTopBar.kt
   │  │  │  ├─ MathQuestionCard.kt
   │  │  │  ├─ NumberPad.kt
   │  │  │  └─ PauseDialog.kt
   │  │  └─ GameScreen.kt
   │  ├─ history
   │  │  ├─ components
   │  │  │  └─ HistorySummaryCard.kt
   │  │  └─ HistoryScreen.kt
   │  ├─ home
   │  │  ├─ components
   │  │  │  ├─ DailyStreakCard.kt
   │  │  │  ├─ HomeHeader.kt
   │  │  │  ├─ RecentScoreItem.kt
   │  │  │  ├─ RecentScoresCard.kt
   │  │  │  └─ StartGameButton.kt
   │  │  └─ HomeScreen.kt
   │  └─ results
   │     ├─ components
   │     │  ├─ CompletionStatusBadge.kt
   │     │  ├─ ResultsActions.kt
   │     │  ├─ ResultsSummaryCard.kt
   │     │  ├─ ResultStatCard.kt
   │     │  ├─ RoundDetailItem.kt
   │     │  └─ RoundDetailsCard.kt
   │     └─ ResultsScreen.kt
   ├─ theme
   │  ├─ Color.kt
   │  ├─ Theme.kt
   │  └─ Type.kt
   └─ utils
      ├─ CompletionStatusUi.kt
      ├─ DifficultyUi.kt
      ├─ LanguagePreferenceUi.kt
      └─ ThemePreferenceUi.kt

```