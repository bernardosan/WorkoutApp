package com.example.workoutapp

class ExerciseModel(
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var restTime: Long,
    private var ExerciseTime: Long,
    private var isSelected: Boolean,
    private var isCompleted: Boolean
){
    fun getId() : Int{
        return id
    }

    fun setId(id: Int){
        this.id = id
    }

    fun getName() : String{
        return name
    }

    fun setName(name: String){
        this.id = id
    }

    fun getImage() : Int{
        return image
    }

    fun setImage(image: Int){
        this.image = image
    }

    fun getIsSelected() : Boolean{
        return isSelected
    }

    fun setIsSelected(sel: Boolean){
        this.isSelected = sel
    }

    fun getIsCompleted() : Boolean{
        return isCompleted
    }

    fun setIsCompleted(com: Boolean){
        this.isCompleted = com
    }

    fun getRestTime() : Long{
        return restTime
    }

    fun setRestTime(rest: Long){
        this.restTime = rest
    }

    fun getExerciseTime() : Long{
        return ExerciseTime
    }

    fun setExerciseTime(exercise: Long){
        this.ExerciseTime = exercise
    }


}