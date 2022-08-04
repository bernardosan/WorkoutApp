package com.example.workoutapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.workoutapp.databinding.ActivityBmiBinding
import kotlin.math.pow
import kotlin.math.roundToInt

class BmiActivity : AppCompatActivity() {
    private var binding: ActivityBmiBinding? = null

    private var is_Unit: Boolean = true
    private var imperial_Unit: Boolean = false
    private var height: Float = 0.0F
    private var weight: Float = 0.0F
    private var bmiResult: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmi)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarBmi?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnCalculateBMI?.setOnClickListener {
            it.hideKeyboard()
            binding?.cvResult?.visibility = View.INVISIBLE
            bmiCalculator()
        }


        binding?.tvUnit?.setOnClickListener {
            if(is_Unit == true) {
                is_Unit = false
                imperial_Unit = true

            } else {
                is_Unit = true
                imperial_Unit = false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

    private fun bmiCalculator(){
        if(binding?.etHeight?.text!!.isEmpty() || binding?.etWeight?.text!!.isEmpty()) {
            Toast.makeText(this, "Enter your Height and Weight!", Toast.LENGTH_LONG).show()
        } else if(binding?.etWeight?.text.toString().toInt() <= 0){
            Toast.makeText(this, "Enter a valid Weight", Toast.LENGTH_LONG).show()
        } else if(binding?.etHeight?.text.toString().toInt() <= 0) {
            Toast.makeText(this, "Enter a valid Height", Toast.LENGTH_LONG).show()
        } else if (is_Unit == true) {
            binding?.cvResult?.visibility = View.VISIBLE
            height = binding?.etHeight?.text!!.toString().toFloat()
            weight = binding?.etWeight?.text!!.toString().toFloat()
            bmiResult = 10000*weight/(height*height)
            binding?.tvBmiResult?.text = "Your BMI is: ${String.format("%.2f", bmiResult).toFloat()}"
            binding?.tvBmiCategory?.text = "Category: ${bmiCategory(bmiResult)}"

        } else if (imperial_Unit== true) {
            binding?.cvResult?.visibility = View.VISIBLE
            height = binding?.etHeight?.text!!.toString().toFloat()
            weight = binding?.etWeight?.text!!.toString().toFloat()
            bmiResult = 703*weight/(height*height)
            binding?.tvBmiResult?.text = "Your BMI is: ${String.format("%.2f", bmiResult).toFloat()}"
            binding?.tvBmiCategory?.text = "Category: ${bmiCategory(bmiResult)}"
        }
    }

    private fun bmiCategory(bmi: Float) : String{
        when{
            bmi < 18.5 -> return "Underweight"
            bmi >= 18.5 &&  bmi < 25 -> return "Normal"
            bmi >= 25 &&  bmi < 30 -> return "Overweight"
            bmi >= 30 &&  bmi < 40 -> return "Obese"
            bmi >= 40 -> return "Morbidly Obese"
        }
        return "Error"
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}


