package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ActivityExerciseBinding
import com.example.workoutapp.databinding.DialogCustomBackConfirmationBinding
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception
import java.text.SimpleDateFormat
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

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

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

        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogBackButton()
    }
    private fun customDialogBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

        if(countDownTimer != null) {
            resetTimer()
        }

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
            tts = null
        }

        if(player != null){
            player?.stop()
            player = null
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

        try{
            val soundURI = Uri.parse("android.resource://com.example.workoutapp/" + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch(e: Exception){
            e.printStackTrace()
        }

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

        exerciseList!![currentExercisePosition].setIsSelected(true)
        exerciseAdapter!!.notifyDataSetChanged()


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

                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseList!![currentExercisePosition].setIsSelected(false)

                exerciseAdapter!!.notifyDataSetChanged()

                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setupRestView()
                } else{
                    Toast.makeText(this@ExerciseActivity, "You finished your Workout!", Toast.LENGTH_LONG).show()
                    val exerciseDao = (application as WorkoutApp).db.exerciseDao()
                    addRecord(exerciseDao)

                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java )
                    startActivity(intent)
                    finish()
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

    fun addRecord(exerciseDao: ExerciseDao) {
        val name = "Exercise"
        val date = getCurrentDateTime().toString("dd/MM/yyyy HH:mm:ss")
        if (name.isNotEmpty() && date.isNotEmpty()) {
            lifecycleScope.launch {
                exerciseDao.insert(ExerciseEntity(name = name, date = date))
                Toast.makeText(applicationContext, "Exercise saved", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Failed to save the exercise",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }



}
