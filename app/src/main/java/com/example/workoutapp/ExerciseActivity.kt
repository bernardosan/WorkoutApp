package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityExerciseBinding
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var countDownTimer: CountDownTimer? = null
    private var pbProgress = 0
    private var restTimerDuration: Long = 5000
    private var exerciseTimerDuration: Long = 10000

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        binding?.tvTimer?.text = (restTimerDuration / 1000).toString()

        startTimerRest(restTimerDuration)


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        tts = TextToSpeech(this@ExerciseActivity, this)
        exerciseList = Constants.defaultExerciseList()
        binding?.tvUpComingLabel?.text = exerciseList!![currentExercisePosition+1].getName()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        if(countDownTimer != null) {
            resetTimer()
        }
    }

    private fun startTimerRest( timerDurationL: Long) {
        var timerInSeconds = timerDurationL/1000

        if(currentExercisePosition >= 0){
            binding?.tvTitleRest?.text = "Rest"
            binding?.tvUpComingLabel?.text = exerciseList!![currentExercisePosition+1].getName()

        }

        speakOut(binding?.tvTitleRest?.text.toString())

        binding?.llExercise?.visibility = View.INVISIBLE
        binding?.llRest?.visibility = View.VISIBLE
        binding?.progressBar?.max = timerInSeconds.toInt() -1
        binding?.progressBar?.progress = 0
        pbProgress = 0

        countDownTimer = object : CountDownTimer(timerDurationL, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pbProgress++
                binding?.tvTimer?.text =(timerInSeconds - pbProgress + 1).toString()
                binding?.progressBar?.progress = (timerInSeconds - pbProgress).toInt()
                if((timerInSeconds - pbProgress) < 3){
                    speakOut((timerInSeconds - pbProgress + 1).toString())
                }
            }

            override fun onFinish() {
                binding?.tvTimer?.text = "-"
                setupExerciseView()
            }
        }.start()
    }

    private fun startTimerExercise( timerDurationL: Long) {

        val timerInSeconds = timerDurationL/1000

        binding?.llExercise?.visibility = View.VISIBLE
        binding?.tvTitleExercise?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.progressBarExercise?.max = timerInSeconds.toInt() -1
        binding?.progressBarExercise?.progress = 0
        pbProgress = 0

        speakOut(exerciseList!![currentExercisePosition].getName())


        countDownTimer = object : CountDownTimer(timerDurationL, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pbProgress++
                binding?.tvTimerExercise?.text =(timerInSeconds - pbProgress + 1).toString()
                binding?.progressBarExercise?.progress = (timerInSeconds - pbProgress).toInt()
                if((timerInSeconds - pbProgress) < 3){
                    speakOut((timerInSeconds - pbProgress + 1).toString())
                }
            }

            override fun onFinish() {
                binding?.tvTimerExercise?.text = "-"
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                } else{
                    Toast.makeText(this@ExerciseActivity, "You finished your Workout!", Toast.LENGTH_LONG).show()
                }

            }
        }.start()
    }

    private fun resetTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null

        }
    }

    private fun setupRestView(){
        binding?.llExercise?.visibility = View.INVISIBLE
        resetTimer()
        restTimerDuration = exerciseList!![currentExercisePosition].getRestTime()
        startTimerRest(restTimerDuration)
    }

    private fun setupExerciseView(){
        currentExercisePosition++
        binding?.llRest?.visibility = View.INVISIBLE
        resetTimer()
        exerciseTimerDuration = exerciseList!![currentExercisePosition].getExerciseTime()
        startTimerExercise(exerciseTimerDuration)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "the language specified is not supported")
            }
        }
    }

    private fun speakOut(s: String) {
        tts?.speak(s, TextToSpeech.QUEUE_FLUSH, null, "")
    }



}
