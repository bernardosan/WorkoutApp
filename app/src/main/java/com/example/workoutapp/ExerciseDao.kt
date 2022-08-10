package com.example.workoutapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insert(exerciseEntity: ExerciseEntity)

    @Update
    suspend fun update(exerciseEntity: ExerciseEntity)

    @Delete
    suspend fun delete(exerciseEntity: ExerciseEntity)

    @Query("SELECT * FROM `EXERCISE-TABLE`")
    fun fetchAllRecords(): Flow<List<ExerciseEntity>>
    // Flow removes the necessity to always notify the recycler view that data changed

    @Query("SELECT * FROM `EXERCISE-TABLE` where id=:id")
    fun fetchRecordById(id:Int): Flow<ExerciseEntity>

}