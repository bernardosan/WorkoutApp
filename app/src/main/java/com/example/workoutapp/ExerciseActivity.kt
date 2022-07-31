package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ActivityExerciseBinding


class ExerciseActivity : AppCompatActivity() {

    private var binding: ActivityExerciseBinding? = null

    private var countDownTimer: CountDownTimer? = null
    private var pbProgress = 0
    private var restTimerDuration: Long = 5000
    private var exerciseTimerDuration: Long = 10000

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var exerciseAdapter : ExerciseStatusAdapter? = null

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

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupExerciseStatusRecyclerView()
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
        }

        binding?.llExercise?.visibility = View.INVISIBLE
        binding?.llRest?.visibility = View.VISIBLE
        binding?.progressBar?.max = timerInSeconds.toInt() -1
        binding?.progressBar?.progress = 0
        pbProgress = 0



        countDownTimer = object : CountDownTimer(timerDurationL, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pbProgress++
                binding?.tvTimer?.text =(timerInSeconds - pbProgress).toString()
                binding?.progressBar?.progress = (timerInSeconds - pbProgress).toInt()
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

        exerciseList!![currentExercisePosition].setIsSelected(true)
        exerciseAdapter!!.notifyDataSetChanged()


        countDownTimer = object : CountDownTimer(timerDurationL, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pbProgress++
                binding?.tvTimerExercise?.text =(timerInSeconds - pbProgress).toString()
                binding?.progressBarExercise?.progress = (timerInSeconds - pbProgress).toInt()
            }

            override fun onFinish() {
                binding?.tvTimerExercise?.text = "-"

                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseList!![currentExercisePosition].setIsSelected(false)

                exerciseAdapter!!.notifyDataSetChanged()

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

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupExerciseView(){
        currentExercisePosition++
        binding?.llRest?.visibility  = View.INVISIBLE
        resetTimer()
        exerciseTimerDuration = exerciseList!![currentExercisePosition].getExerciseTime()
        startTimerExercise(exerciseTimerDuration)
    }



}
