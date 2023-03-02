package com.example.receipeapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealAdapter(private val context: Context, private val mealList : List<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {
    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val mealImageView : ImageView = itemView.findViewById(R.id.mealImage)
        val mealNameTv : TextView = itemView.findViewById(R.id.mealTitle)
        val mealCalTv : TextView = itemView.findViewById(R.id.caloriesTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meals = mealList[position]

        holder.mealNameTv.text = meals.title
        holder.mealCalTv.text = meals.calories + " kcal"

        Glide.with(context)
            .load(meals.image)
            .into(holder.mealImageView)

        holder.itemView.setOnClickListener{
            val intent = Intent(context,RecipeActivity::class.java)
            intent.putExtra("title",meals.title)
            intent.putExtra("cal",meals.calories)
            intent.putExtra("image",meals.image)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}