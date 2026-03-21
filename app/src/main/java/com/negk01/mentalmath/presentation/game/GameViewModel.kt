package com.negk01.mentalmath.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.game.QuestionGenerator
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.Question
import com.negk01.mentalmath.domain.model.RoundResult
import com.negk01.mentalmath.domain.model.getGameConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var currentQuestion: Question? = null
    private val roundResults = mutableListOf<RoundResult>()

    private var currentDifficulty: Difficulty = Difficulty.EASY
    private var currentTimePerRound = 30

    private var timerJob: Job? = null

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var _sessionResult: GameSessionResult? = null
    val sessionResult: GameSessionResult?
        get() = _sessionResult

    init {
        startGame(Difficulty.EASY)
    }

    fun startGame(difficulty: Difficulty) {
        timerJob?.cancel()

        val config = getGameConfig(difficulty)
        currentDifficulty = difficulty
        currentTimePerRound = config.timePerRoundSeconds
        roundResults.clear()
        _sessionResult = null

        val firstQuestion = QuestionGenerator.generate(difficulty)
        currentQuestion = firstQuestion

        _uiState.value = GameUiState(
            difficulty = difficulty,
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
        startGame(currentDifficulty)
    }

    fun onDigitPressed(digit: String) {
        if (_uiState.value.isPaused || _uiState.value.isFinished) return

        _uiState.update { currentState ->
            currentState.copy(
                answerInput = currentState.answerInput + digit
            )
        }
    }

    fun onClearDigit() {
        if (_uiState.value.isPaused || _uiState.value.isFinished) return

        _uiState.update { currentState ->
            currentState.copy(
                answerInput = currentState.answerInput.dropLast(1)
            )
        }
    }

    fun onSubmitAnswer() {
        if (_uiState.value.isPaused || _uiState.value.isFinished) return
        if (_uiState.value.answerInput.isBlank()) return

        resolveCurrentRound(userRanOutOfTime = false)
    }

    fun onTimeExpired() {
        if (_uiState.value.isPaused || _uiState.value.isFinished) return
        resolveCurrentRound(userRanOutOfTime = true)
    }

    private fun resolveCurrentRound(userRanOutOfTime: Boolean) {
        timerJob?.cancel()

        val state = _uiState.value
        val question = currentQuestion ?: return

        val answer = when {
            state.answerInput.isBlank() -> 0
            else -> state.answerInput.toIntOrNull() ?: 0
        }

        val timeSpent = currentTimePerRound - state.timeLeftSeconds
        val consumedTime = if (userRanOutOfTime) currentTimePerRound else timeSpent

        val roundResult = RoundResult(
            roundNumber = state.currentRound,
            question = question.expression,
            userAnswer = answer,
            correctAnswer = question.correctAnswer,
            isCorrect = answer == question.correctAnswer,
            timeSpentSeconds = consumedTime
        )

        roundResults.add(roundResult)

        val isLastRound = state.currentRound >= state.totalRounds

        if (isLastRound) {
            finishGame(completionStatus = "completed")
            return
        }

        val nextQuestion = QuestionGenerator.generate(currentDifficulty)
        currentQuestion = nextQuestion

        _uiState.update { currentState ->
            currentState.copy(
                currentRound = currentState.currentRound + 1,
                timeLeftSeconds = currentTimePerRound,
                questionText = nextQuestion.expression,
                answerInput = "",
                isPaused = false
            )
        }

        startRoundTimer()
    }

    fun abandonGame(): Boolean {
        timerJob?.cancel()

        if (roundResults.isEmpty()) return false

        finishGame(completionStatus = "abandoned")
        return true
    }

    private fun finishGame(completionStatus: String) {
        timerJob?.cancel()

        val correctAnswers = roundResults.count { it.isCorrect }
        val averageTime = if (roundResults.isEmpty()) {
            0.0
        } else {
            roundResults.map { it.timeSpentSeconds }.average()
        }

        _sessionResult = GameSessionResult(
            difficulty = currentDifficulty,
            correctAnswers = correctAnswers,
            totalRounds = _uiState.value.totalRounds,
            averageResponseTimeSeconds = averageTime,
            completionStatus = completionStatus,
            roundResults = roundResults.toList()
        )

        _uiState.update { currentState ->
            currentState.copy(
                isFinished = true,
                answerInput = ""
            )
        }
    }

    fun pauseGame() {
        if (_uiState.value.isFinished) return

        timerJob?.cancel()

        _uiState.update { currentState ->
            currentState.copy(isPaused = true)
        }
    }

    fun resumeGame() {
        if (_uiState.value.isFinished) return

        _uiState.update { currentState ->
            currentState.copy(isPaused = false)
        }

        startRoundTimer()
    }

    private fun startRoundTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0 &&
                !_uiState.value.isPaused &&
                !_uiState.value.isFinished
            ) {
                delay(1000)
                val currentState = _uiState.value

                if (currentState.isPaused || currentState.isFinished) break

                val newTime = currentState.timeLeftSeconds - 1

                _uiState.update {
                    it.copy(timeLeftSeconds = newTime)
                }

                if (newTime <= 0) {
                    onTimeExpired()
                    break
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}