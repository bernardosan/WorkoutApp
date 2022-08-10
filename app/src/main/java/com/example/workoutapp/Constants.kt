package com.example.workoutapp

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        val exercise1 = ExerciseModel(1,"Arm Stretching", R.drawable.exercise1, 5000, 10000, false, false )
        exerciseList.add(exercise1)
        val exercise2 = ExerciseModel(2,"Leg Stretching", R.drawable.exercise2, 1000, 1000, false, false )
        exerciseList.add(exercise2)
        val exercise3 = ExerciseModel(3,"Back Stretching", R.drawable.exercise3, 1000, 1000, false, false )
        exerciseList.add(exercise3)
        val exercise4 = ExerciseModel(4,"Sit Ups", R.drawable.exercise4,1000, 1000, false, false )
        exerciseList.add(exercise4)
        val exercise5 = ExerciseModel(5,"Planking", R.drawable.exercise5, 1000, 2000, false, false )
        exerciseList.add(exercise5)
        val exercise6 = ExerciseModel(6,"Squating", R.drawable.exercise6, 1000, 1000, false, false )
        exerciseList.add(exercise6)
        val exercise7 = ExerciseModel(7,"Running", R.drawable.exercise7, 100, 1000, false, false )
        exerciseList.add(exercise7)

        return exerciseList
    }

}