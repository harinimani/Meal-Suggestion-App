package com.example.receipeapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryAdapter(private var categoryList : List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var onItemClick: ((Category) -> Unit)? = null


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryImageView : ImageView = itemView.findViewById(R.id.categoryID)
        val categoryNameTv : TextView = itemView.findViewById(R.id.categoryText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryImageView.setImageResource(category.categoryImage)
        holder.categoryNameTv.text = category.categoryName

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(categoryList[position])
        }


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}