package com.example.myapplication

class CalculatorService {

    fun calculateFinalGrade(
        midterm1: Double = 100.0,
        midterm2: Double = 100.0,
        participation: Double = 100.0,
        groupPresentation: Double = 100.0,
        finalProject: Double = 100.0,
        homework: MutableList<Double>
    ): Double {
        while (homework.size != 5) {
            homework.add(100.0)
        }

        val homeworkAverage = homework.sum() / homework.size

        return ((midterm1 * 10) + (midterm2 * 20) + (participation * 10) +
                (groupPresentation * 10) + (finalProject * 30) + (homeworkAverage * 20)) / 100
    }
}