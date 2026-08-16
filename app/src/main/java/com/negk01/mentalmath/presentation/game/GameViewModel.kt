package com.negk01.mentalmath.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.game.QuestionGenerator
import com.negk01.mentalmath.domain.game.StreakTracker
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.Question
import com.negk01.mentalmath.domain.model.RoundResult
import com.negk01.mentalmath.domain.model.getGameConfig
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlin.math.ceil
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ANSWER_FLASH_MILLIS = 300L

class GameViewModel(
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    private var currentQuestion: Question? = null
    private val roundResults = mutableListOf<RoundResult>()

    private var currentDifficulty: Difficulty = Difficulty.EASY
    private var currentTimePerRound = 30
    private val streakTracker = StreakTracker()

    private var roundStartTimeMillis = 0L
    private var accumulatedRoundTimeMillis = 0L

    private var timerJob: Job? = null
    private var flashJob: Job? = null

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun startGame(difficulty: Difficulty) {
        timerJob?.cancel()
        flashJob?.cancel()

        val config = getGameConfig(difficulty)
        currentDifficulty = difficulty
        currentTimePerRound = config.timePerRoundSeconds
        streakTracker.reset()
        roundResults.clear()
        accumulatedRoundTimeMillis = 0L
        roundStartTimeMillis = System.currentTimeMillis()

        val firstQuestion = QuestionGenerator.generate(difficulty)
        currentQuestion = firstQuestion

        _uiState.value = GameUiState(
            currentRound = 1,
            totalRounds = config.totalRounds,
            timeLeftSeconds = config.timePerRoundSeconds,
            questionText = firstQuestion.expression,
            answerInput = "",
            isPaused = false,
            isFinished = false
        )

        startRoundTimer()
    }

    fun restartGame() {
        flashJob?.cancel()

        startGame(currentDifficulty)
    }

    fun onDigitPressed(digit: String) {
        val state = _uiState.value

        if (_uiState.value.isInputLocked) return

        if (state.isPaused || state.isFinished) return

        if (state.answerInput.length >= 5) return

        val newInput = if (state.answerInput == "0") {
            digit
        } else {
            state.answerInput + digit
        }

        _uiState.update {
            it.copy(
                answerInput = newInput
            )
        }
    }

    fun onClearDigit() {
        if (_uiState.value.isPaused || _uiState.value.isFinished) return

        if (_uiState.value.isInputLocked) return

        _uiState.update { currentState ->
            currentState.copy(
                answerInput = currentState.answerInput.dropLast(1)
            )
        }
    }

    fun onSubmitAnswer() {
        val state = _uiState.value
        if (state.isPaused || state.isFinished || state.isInputLocked) return
        if (state.answerInput.isBlank()) return

        val question = currentQuestion ?: return

        val answer = state.answerInput.toIntOrNull() ?: 0
        val isCorrect = answer == question.correctAnswer

        triggerAnswerFlash(isCorrect = isCorrect, userRanOutOfTime = false)
    }

    private fun onTimeExpired() {
        val state = _uiState.value
        val question = currentQuestion ?: return

        val answer = state.answerInput.toIntOrNull() ?: 0
        val isCorrect = answer == question.correctAnswer

        triggerAnswerFlash(isCorrect = isCorrect, userRanOutOfTime = true)
    }

    private fun triggerAnswerFlash(isCorrect: Boolean, userRanOutOfTime: Boolean) {
        timerJob?.cancel()

        val totalRoundTimeMillis = currentTimePerRound * 1000L
        val consumedMillis = if (userRanOutOfTime) {
            totalRoundTimeMillis
        } else {
            val currentElapsed = accumulatedRoundTimeMillis + (System.currentTimeMillis() - roundStartTimeMillis)
            currentElapsed.coerceIn(0L, totalRoundTimeMillis)
        }

        // 1. Lock input and show flash color
        _uiState.update {
            it.copy(
                lastAnswerCorrect = isCorrect,
                isInputLocked = true
            )
        }

        flashJob?.cancel()
        flashJob = viewModelScope.launch {
            delay(ANSWER_FLASH_MILLIS)
            resolveCurrentRound(consumedMillis = consumedMillis)
        }
    }

    private fun resolveCurrentRound(consumedMillis: Long) {
        val state = _uiState.value
        val question = currentQuestion ?: return

        val answer = when {
            state.answerInput.isBlank() -> 0
            else -> state.answerInput.toIntOrNull() ?: 0
        }

        val isCorrect = answer == question.correctAnswer

        streakTracker.recordAnswer(isCorrect)

        val roundResult = RoundResult(
            question = question.expression,
            userAnswer = answer,
            correctAnswer = question.correctAnswer,
            isCorrect = isCorrect,
            timeSpentMillis = consumedMillis,
            operator = question.operator
        )

        roundResults.add(roundResult)

        val isLastRound = state.currentRound >= state.totalRounds

        if (isLastRound) {
            finishGame(completionStatus = CompletionStatus.COMPLETED)
            return
        }

        val nextQuestion = QuestionGenerator.generate(currentDifficulty)
        currentQuestion = nextQuestion

        roundStartTimeMillis = System.currentTimeMillis()
        accumulatedRoundTimeMillis = 0L

        _uiState.update { currentState ->
            currentState.copy(
                currentRound = currentState.currentRound + 1,
                timeLeftSeconds = currentTimePerRound,
                questionText = nextQuestion.expression,
                answerInput = "",
                lastAnswerCorrect = null,
                isInputLocked = false
            )
        }

        startRoundTimer()
    }

    fun abandonGame(): Boolean {
        timerJob?.cancel()
        flashJob?.cancel()

        if (roundResults.isEmpty()) return false

        finishGame(completionStatus = CompletionStatus.ABANDONED)
        return true
    }

    private fun finishGame(completionStatus: CompletionStatus) {
        timerJob?.cancel()

        val sessionResult = GameSessionResult.fromRounds(
            difficulty = currentDifficulty,
            totalRounds = _uiState.value.totalRounds,
            completionStatus = completionStatus,
            roundResults = roundResults.toList(),
            maxStreak = streakTracker.maxStreak
        )

        if (completionStatus == CompletionStatus.COMPLETED) {
            saveCompletedGame(sessionResult)
        }

        _uiState.update { currentState ->
            currentState.copy(
                isFinished = true,
                answerInput = "",
                sessionResult = sessionResult
            )
        }
    }

    private fun saveCompletedGame(sessionResult: GameSessionResult) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                gameRecordRepository.insert(
                    GameRecord.fromCompletedSession(
                        difficulty = sessionResult.difficulty,
                        correctAnswers = sessionResult.correctAnswers,
                        totalRounds = sessionResult.totalRounds,
                        averageResponseTimeMillis = sessionResult.averageResponseTimeMillis,
                        maxStreak = sessionResult.maxStreak
                    )
                )
            }
        }
    }

    fun pauseGame() {
        if (_uiState.value.isPaused) return
        if (_uiState.value.isFinished) return
        if (_uiState.value.isInputLocked) return

        timerJob?.cancel()
        accumulatedRoundTimeMillis += (System.currentTimeMillis() - roundStartTimeMillis)

        _uiState.update { currentState ->
            currentState.copy(
                isPaused = true,
                lastAnswerCorrect = null,
                isInputLocked = false
            )
        }
    }

    fun resumeGame() {
        if (!_uiState.value.isPaused) return
        if (_uiState.value.isFinished) return

        roundStartTimeMillis = System.currentTimeMillis()

        _uiState.update { currentState ->
            currentState.copy(isPaused = false)
        }

        startRoundTimer()
    }

    private fun startRoundTimer() {
        timerJob?.cancel()

        val totalRoundTimeMillis = currentTimePerRound * 1000L

        timerJob = viewModelScope.launch {
            while (!_uiState.value.isPaused && !_uiState.value.isFinished) {
                val elapsedMillis = accumulatedRoundTimeMillis + (System.currentTimeMillis() - roundStartTimeMillis)
                val remainingMillis = (totalRoundTimeMillis - elapsedMillis).coerceAtLeast(0L)
                val remainingSeconds = ceil(remainingMillis / 1000.0).toInt()

                _uiState.update {
                    it.copy(timeLeftSeconds = remainingSeconds)
                }

                if (remainingMillis <= 0L) {
                    onTimeExpired()
                    break
                }
                delay(100L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        flashJob?.cancel()
    }
}
