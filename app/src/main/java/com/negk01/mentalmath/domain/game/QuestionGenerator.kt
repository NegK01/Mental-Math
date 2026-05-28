package com.negk01.mentalmath.domain.game

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.Operator
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
        val useCompound = Random.nextBoolean()

        if (!useCompound) {
            return simpleQuestion(range = 5..30, allowNegative = false)
        }

        val a = Random.nextInt(5, 31)
        val b = Random.nextInt(2, 21)
        val c = Random.nextInt(2, 21)

        return when (Random.nextInt(4)) {
            0 -> {
                val expression = "$a + $b + $c"
                val answer = a + b + c
                Question(expression, answer, Operator.ADD)
            }

            1 -> {
                val expression = "$a - $b + $c"
                val answer = a - b + c
                if (answer < 0) generateMediumQuestion() else Question(expression, answer, Operator.SUBTRACT)
            }

            2 -> {
                val expression = "$a + $b × $c"
                val answer = a + (b * c)
                Question(expression, answer, Operator.MULTIPLY)
            }

            else -> {
                val expression = "$a × $b - $c"
                val answer = (a * b) - c
                if (answer < 0) generateMediumQuestion() else Question(expression, answer, Operator.MULTIPLY)
            }
        }
    }

    private fun generateHardQuestion(): Question {
        val type = Random.nextInt(5)

        return when (type) {
            0 -> {
                val a = Random.nextInt(2, 21)
                val b = Random.nextInt(2, 11)
                val c = Random.nextInt(2, 16)
                val d = Random.nextInt(2, 11)
                val expression = "$a × $b - $c + $d"
                val answer = a * b - c + d
                if (answer < 0) generateHardQuestion() else Question(expression, answer, Operator.MULTIPLY)
            }

            1 -> {
                val a = Random.nextInt(2, 31)
                val b = Random.nextInt(2, 13)
                val c = Random.nextInt(2, 13)
                val expression = "$a + $b × $c"
                val answer = a + (b * c)
                Question(expression, answer, Operator.MULTIPLY)
            }

            2 -> {
                val a = Random.nextInt(2, 31)
                val divisor = Random.nextInt(2, 13)
                val quotient = Random.nextInt(2, 13)
                val dividend = divisor * quotient
                val expression = "$a + $dividend ÷ $divisor"
                val answer = a + (dividend / divisor)
                Question(expression, answer, Operator.DIVIDE)
            }

            3 -> {
                val a = Random.nextInt(2, 21)
                val b = Random.nextInt(2, 13)
                val c = Random.nextInt(2, 21)
                val expression = "($a + $b) × $c"
                val answer = (a + b) * c
                Question(expression, answer, Operator.MULTIPLY)
            }

            else -> {
                val a = Random.nextInt(5, 31)
                val b = Random.nextInt(2, 21)
                val c = Random.nextInt(2, 21)
                val d = Random.nextInt(2, 16)
                val expression = "$a - $b + $c × $d"
                val answer = a - b + (c * d)
                if (answer < 0) generateHardQuestion() else Question(expression, answer, Operator.MULTIPLY)
            }
        }
    }

    private fun simpleQuestion(range: IntRange, allowNegative: Boolean): Question {
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
        return Question("$a + $b", a + b, Operator.ADD)
    }

    private fun generateSubtractionQuestion(range: IntRange, allowNegative: Boolean): Question {
        val a = range.random()
        val b = range.random()
        return if (!allowNegative && a < b) {
            Question("$b - $a", b - a, Operator.SUBTRACT)
        } else {
            Question("$a - $b", a - b, Operator.SUBTRACT)
        }
    }

    private fun generateMultiplicationQuestion(leftRange: IntRange, rightRange: IntRange): Question {
        val a = leftRange.random()
        val b = rightRange.random()
        return Question("$a × $b", a * b, Operator.MULTIPLY)
    }

    private fun generateDivisionQuestion(quotientRange: IntRange, divisorRange: IntRange): Question {
        val quotient = quotientRange.random()
        val divisor = divisorRange.random()
        val dividend = quotient * divisor
        return Question("$dividend ÷ $divisor", quotient, Operator.DIVIDE)
    }
}
