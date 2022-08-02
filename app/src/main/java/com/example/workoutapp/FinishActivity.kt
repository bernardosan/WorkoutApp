package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import com.example.workoutapp.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flFinish?.setOnClickListener{
            val intent = Intent(this@FinishActivity, MainActivity::class.java )
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }
}