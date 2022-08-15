package com.example.workoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ItemsRowBinding

class ExerciseHistoryAdapter (private val items: ArrayList<ExerciseEntity>,
                              //private val updateListener:(id:Int)->Unit,
                              private val deleteListener:(id:Int)->Unit
): RecyclerView.Adapter<ExerciseHistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvDate = binding.tvDate
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvName.text = item.name + item.id.toString()
        holder.tvDate.text = item.date

        if(position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGray))
        } else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.ivEdit.setOnClickListener {
            //updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}