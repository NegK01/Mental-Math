package com.negk01.mentalmath.domain.game

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.Question
import kotlin.random.Random

object QuestionGenerator {

    fun generate(difficulty: Difficulty): Question {
        return when (difficulty) {
            Difficulty.EASY -> generateEasyQuestion()
            Difficulty.MEDIUM -> generateMediumQuestion()
            Difficulty.HARD -> generateHardQuestion()
        }
    }

    private fun generateEasyQuestion(): Question {
        return when (Random.nextInt(4)) {
            0 -> generateAdditionQuestion(1..20)
            1 -> generateSubtractionQuestion(1..20, allowNegative = false)
            2 -> generateMultiplicationQuestion(1..10, 1..10)
            else -> generateDivisionQuestion(1..10, 1..10)
        }
    }

    private fun generateMediumQuestion(): Question {
        val first = simpleQuestion(5..30, allowNegative = false)
        val second = simpleQuestion(2..20, allowNegative = false)

        val useCompound = Random.nextBoolean()

        if (!useCompound) return first

        val operators = listOf("+", "-", "×")
        val operator = operators.random()
        val expression = "${first.expression} $operator ${extractRightValue(second.expression)}"

        val correctAnswer = when (operator) {
            "+" -> first.correctAnswer + extractQuestionValue(second)
            "-" -> first.correctAnswer - extractQuestionValue(second)
            else -> first.correctAnswer * extractQuestionValue(second)
        }

        if (correctAnswer < 0) return generateMediumQuestion()

        return Question(expression, correctAnswer)
    }

    private fun generateHardQuestion(): Question {
        val a = Random.nextInt(2, 21)
        val b = Random.nextInt(2, 11)
        val c = Random.nextInt(2, 16)
        val d = Random.nextInt(2, 11)

        val type = Random.nextInt(3)

        return when (type) {
            0 -> {
                val expression = "$a × $b - $c + $d"
                val answer = a * b - c + d
                Question(expression, answer)
            }

            1 -> {
                val expression = "$a + ($b × $c)"
                val answer = a + (b * c)
                Question(expression, answer)
            }

            else -> {
                val dividend = b * c
                val expression = "$a + ($dividend ÷ $b)"
                val answer = a + (dividend / b)
                Question(expression, answer)
            }
        }
    }

    private fun simpleQuestion(
        range: IntRange,
        allowNegative: Boolean
    ): Question {
        return when (Random.nextInt(4)) {
            0 -> generateAdditionQuestion(range)
            1 -> generateSubtractionQuestion(range, allowNegative)
            2 -> generateMultiplicationQuestion(2..12, 2..12)
            else -> generateDivisionQuestion(2..12, 2..12)
        }
    }

    private fun generateAdditionQuestion(range: IntRange): Question {
        val a = range.random()
        val b = range.random()
        return Question("$a + $b", a + b)
    }

    private fun generateSubtractionQuestion(
        range: IntRange,
        allowNegative: Boolean
    ): Question {
        val a = range.random()
        val b = range.random()

        return if (!allowNegative && a < b) {
            Question("$b - $a", b - a)
        } else {
            Question("$a - $b", a - b)
        }
    }

    private fun generateMultiplicationQuestion(
        leftRange: IntRange,
        rightRange: IntRange
    ): Question {
        val a = leftRange.random()
        val b = rightRange.random()
        return Question("$a × $b", a * b)
    }

    private fun generateDivisionQuestion(
        quotientRange: IntRange,
        divisorRange: IntRange
    ): Question {
        val quotient = quotientRange.random()
        val divisor = divisorRange.random()
        val dividend = quotient * divisor
        return Question("$dividend ÷ $divisor", quotient)
    }

    private fun extractRightValue(expression: String): String {
        return expression.substringAfterLast(" ")
    }

    private fun extractQuestionValue(question: Question): Int {
        return question.correctAnswer
    }
}