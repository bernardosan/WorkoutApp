package com.example.workoutapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.workoutapp.databinding.ActivityBmiBinding

class BmiActivity : AppCompatActivity() {
    private var binding: ActivityBmiBinding? = null

    private var isMetric: Boolean = true
    private var isImperial: Boolean = false
    private var height: Float? = 0.0F
    private var heightInches: Float? = 0.0F
    private var weight: Float? = 0.0F
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
            if(isMetric == true){
                bmiCalculatorMetric()
            } else if(isImperial == true){
                bmiCalculatorImperial()
            }
        }


        binding?.tvUnit?.setOnClickListener {
            if(isMetric == true) {
                toImperialWindow()

            } else {
                toMetricWindow()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

    private fun bmiCalculatorMetric(){

        height = binding?.etHeight?.text!!.toString().toFloat()
        weight = binding?.etWeight?.text!!.toString().toFloat()

        if(height == null || weight == null) {
            Toast.makeText(this, "Enter your Height and Weight!", Toast.LENGTH_LONG).show()
        } else if(weight!! <= 0){
            Toast.makeText(this, "Enter a valid Weight", Toast.LENGTH_LONG).show()
        } else if(weight!! <= 0) {
            Toast.makeText(this, "Enter a valid Height", Toast.LENGTH_LONG).show()
        } else if (isMetric == true) {
            binding?.cvResult?.visibility = View.VISIBLE
            bmiResult = 10000*weight!!/(height!!*height!!)
            binding?.tvBmiResult?.text = "Your BMI is: ${String.format("%.2f", bmiResult)}"
            binding?.tvBmiCategory?.text = "Category: ${bmiCategory(bmiResult)}"

        }
    }

    private fun bmiCalculatorImperial(){

        height = binding?.etImperialHeightFeet?.text!!.toString().toFloat()
        heightInches = binding?.etImperialHeightInches?.text!!.toString().toFloat()
        weight = binding?.etImperialWeight?.text!!.toString().toFloat()

        if(height == null && heightInches == null && weight == null ) {
            Toast.makeText(this, "Enter your Height and Weight!", Toast.LENGTH_LONG).show()
        } else if( weight!! <= 0){
            Toast.makeText(this, "Enter a valid Weight", Toast.LENGTH_LONG).show()
        } else if( height!! <= 0 || heightInches!! <= 0) {
            Toast.makeText(this, "Enter a valid Height", Toast.LENGTH_LONG).show()
        }  else if (isImperial == true) {
            binding?.cvResult?.visibility = View.VISIBLE
            height = height!!*12 + heightInches!!
            bmiResult = 703* weight!!/(height!!*height!!)
            binding?.tvBmiResult?.text = "Your BMI is: ${String.format("%.2f", bmiResult)}"
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

    private fun toMetricWindow(){
        isMetric = true
        isImperial = false
        binding?.cvResult?.visibility = View.GONE
        binding?.tilMetricHeight?.visibility = View.VISIBLE
        binding?.tilMetricWeight?.visibility = View.VISIBLE
        binding?.tilImperialHeightFeet?.visibility = View.GONE
        binding?.tilImperialHeightInches?.visibility = View.GONE
        binding?.tilImperialWeight?.visibility = View.GONE
    }

    private fun toImperialWindow(){
        isMetric = false
        isImperial = true
        binding?.cvResult?.visibility = View.GONE
        binding?.tilMetricHeight?.visibility = View.GONE
        binding?.tilMetricHeight?.visibility = View.GONE
        binding?.tilMetricWeight?.visibility = View.GONE
        binding?.tilImperialHeightFeet?.visibility = View.VISIBLE
        binding?.tilImperialHeightInches?.visibility = View.VISIBLE
        binding?.tilImperialWeight?.visibility = View.VISIBLE
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}


