package com.example.workoutapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        val exerciseDao = (application as WorkoutApp).db.exerciseDao()

        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistory)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launch {
             exerciseDao.fetchAllRecords().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, exerciseDao)
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

    private fun setupListOfDataIntoRecyclerView(
        exerciseList: ArrayList<ExerciseEntity>,
        exerciseDao: ExerciseDao
    ) {

        if (exerciseList.isNotEmpty()) {
            val exerciseHistoryAdapter = ExerciseHistoryAdapter(exerciseList, { deleteId -> deleteRecordDialog(deleteId, exerciseDao)} )

            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvExerciseList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
            binding?.rvExerciseList?.adapter = exerciseHistoryAdapter
            binding?.rvExerciseList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {

            binding?.rvExerciseList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun deleteRecordDialog(id: Int, exerciseDao: ExerciseDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_delete)
        builder.setPositiveButton("Yes"){ dialogInterface,_ ->
            lifecycleScope.launch {
                exerciseDao.delete(ExerciseEntity(id))
                Toast.makeText(applicationContext, "Record deleted successfully", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){ dialogInterface,_ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}


