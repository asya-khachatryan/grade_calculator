package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, homework
        )
        listView.adapter = arrayAdapter
        sharedPreferences = getSharedPreferences("grade", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("initialized", false)) {
            loadData()
        }
    }

    private val homework = mutableListOf<Double>()
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<Double>
    private lateinit var sharedPreferences: SharedPreferences
    private val calculatorService: CalculatorService = CalculatorService()

    fun calculate(view: View) {

        val midterm1 = try {
            findViewById<EditText>(R.id.midterm1).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            100.0
        }

        val midterm2 = try {
            findViewById<EditText>(R.id.midterm2).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            100.0
        }

        val participation = try {
            findViewById<EditText>(R.id.participation).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            100.0
        }

        val groupPresentation = try {
            findViewById<EditText>(R.id.groupPresentation).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            100.0
        }

        val finalProject = try {
            findViewById<EditText>(R.id.finalProject).text.toString().toDouble()
        } catch (e: NumberFormatException) {
            100.0
        }

        if (!inputIsValid(midterm1, midterm2, participation, groupPresentation, finalProject)){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Invalid input")
            builder.setMessage("The grades should be in the range 0 - 100")
            builder.create().show()
        } else {

            val finalGrade = calculatorService.calculateFinalGrade(
                midterm1, midterm2, participation, groupPresentation, finalProject, homework
            )
            saveData(midterm1, midterm2, participation, groupPresentation, finalProject, homework)

            val final = findViewById<TextView>(R.id.finalGrade)

            final.text = finalGrade.toString()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Final Grade")
            builder.setMessage("Your final grade is $finalGrade")
            builder.create().show()
        }
    }

    private fun inputIsValid(
        midterm1: Double,
        midterm2: Double,
        participation: Double,
        groupPresentation: Double,
        finalProject: Double,
    ): Boolean {
        if (midterm1 !in 0.0..100.0
            || midterm2 !in 0.0..100.0
            || participation !in 0.0..100.0
            || groupPresentation !in 0.0..100.0
            || finalProject !in 0.0..100.0){
            return false
        }
        return true
    }

    private fun inputIsValid(
        homework: Double
    ): Boolean {
        if (homework !in 0.0..100.0){
            return false
        }
        return true
    }

    fun clear(view: View) {
        val midterm1 = findViewById<EditText>(R.id.midterm1)
        val midterm2 = findViewById<EditText>(R.id.midterm2)
        val participation = findViewById<EditText>(R.id.participation)
        val groupPresentation =
            findViewById<EditText>(R.id.groupPresentation)
        val finalProject = findViewById<EditText>(R.id.finalProject)

        midterm1.setText("")
        midterm2.setText("")
        participation.setText("")
        groupPresentation.setText("")
        finalProject.setText("")
        midterm1.requestFocus()

        homework.clear()
        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, homework
        )
        findViewById<EditText>(R.id.hw).setBackgroundColor(Color.WHITE)
    }

    fun addHomework(view: View) {
//        listView = findViewById<ListView>(R.id.listView)
        val hwGradeEditText = findViewById<EditText>(R.id.hw)
        hwGradeEditText.setBackgroundColor(Color.WHITE)
        if (homework.size == 5) {
            hwGradeEditText.setBackgroundColor(Color.RED)
            hwGradeEditText.setText("")
        } else {
            val grade = hwGradeEditText.text.toString()
            if (grade == "") {
                hwGradeEditText.setBackgroundColor(Color.RED)
            }
            if (!inputIsValid(grade.toDouble())){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Invalid input")
                builder.setMessage("The homework grade should be in the range 0 - 100")
                builder.create().show()
            } else {
                homework.add(grade.toDouble())
                arrayAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1, homework
                )
                listView.adapter = arrayAdapter
                hwGradeEditText.setText("")
            }
        }
    }

    fun resetHomeworks(view: View) {
        homework.clear()
        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, homework
        )
        findViewById<EditText>(R.id.hw).setBackgroundColor(Color.WHITE)
    }


    private fun saveData(
        midterm1: Double = 100.0,
        midterm2: Double = 100.0,
        participation: Double = 100.0,
        groupPresentation: Double = 100.0,
        finalProject: Double = 100.0,
        homework: MutableList<Double>
    ) {
        sharedPreferences = getSharedPreferences("grade", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("initialized", true)
        editor.putFloat("midterm1", midterm1.toFloat())
        editor.putFloat("midterm2", midterm2.toFloat())
        editor.putFloat("participation", participation.toFloat())
        editor.putFloat("groupPresentation", groupPresentation.toFloat())
        editor.putFloat("finalProject", finalProject.toFloat())
        for (i in 1..5) {
            editor.putFloat("hw$i", homework[i - 1].toFloat())
        }
        editor.apply()
    }

    private fun loadData() {
        val midterm1 = sharedPreferences.getFloat("midterm1", 100.0F)
        val midterm2 = sharedPreferences.getFloat("midterm2", 100.0F)
        val participation = sharedPreferences.getFloat("participation", 100.0F)
        val groupPresentation = sharedPreferences.getFloat("groupPresentation", 100.0F)
        val finalProject = sharedPreferences.getFloat("finalProject", 100.0F)
        val hw1 = sharedPreferences.getFloat("hw1", 100.0F)
        val hw2 = sharedPreferences.getFloat("hw2", 100.0F)
        val hw3 = sharedPreferences.getFloat("hw3", 100.0F)
        val hw4 = sharedPreferences.getFloat("hw4", 100.0F)
        val hw5 = sharedPreferences.getFloat("hw5", 100.0F)


        findViewById<EditText>(R.id.midterm1).setText(midterm1.toString())
        findViewById<EditText>(R.id.midterm2).setText(midterm2.toString())
        findViewById<EditText>(R.id.participation).setText(participation.toString())
        findViewById<EditText>(R.id.groupPresentation).setText(groupPresentation.toString())
        findViewById<EditText>(R.id.finalProject).setText(finalProject.toString())

        homework.add(hw1.toDouble())
        homework.add(hw2.toDouble())
        homework.add(hw3.toDouble())
        homework.add(hw4.toDouble())
        homework.add(hw5.toDouble())

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, homework
        )
    }


}