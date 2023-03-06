package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test


internal class MainActivityTest {

    private val calculatorService: CalculatorService = CalculatorService()
    private val DELTA = 1e-15

    @Test
    fun calculate() {
        val midterm1 = 70.0
        val midterm2 = 80.0
        val participation = 90.0
        val groupPresentation = 97.0
        val finalProject = 88.0
        val homework = mutableListOf(100.0, 100.0, 93.0, 89.0, 90.0)

        val grade = calculatorService.calculateFinalGrade(
            midterm1,
            midterm2,
            participation,
            groupPresentation,
            finalProject,
            homework
        )
        assertEquals(86.98, grade, DELTA)
    }
}