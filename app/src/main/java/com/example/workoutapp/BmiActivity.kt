package com.example.workoutapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.workoutapp.databinding.ActivityBmiBinding

class BmiActivity : AppCompatActivity() {
    private var binding: ActivityBmiBinding? = null

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
            if(binding?.cvResult?.visibility == View.VISIBLE) {
                binding?.cvResult?.visibility = View.INVISIBLE
            } else{
                binding?.cvResult?.visibility = View.VISIBLE
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }
}


