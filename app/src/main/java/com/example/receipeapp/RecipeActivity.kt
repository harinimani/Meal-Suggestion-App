package com.example.receipeapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RecipeActivity : AppCompatActivity() {

    private var docID: String = ""
    private var mealText: String = ""
    private var ingredients: List<String> = emptyList()
    private var instructions: List<String> = emptyList()
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val imageView=findViewById<ImageView>(R.id.mealImageScreen);
        val mealTitle=findViewById<TextView>(R.id.mealNametitle);
        val mealkcal=findViewById<TextView>(R.id.mealCaloriesInfo);
        val mealIngredients=findViewById<TextView>(R.id.mealIngredients);
        val mealInstructions=findViewById<TextView>(R.id.mealInstructions);

        //placing the information received from the explicit intent into the components.
        val mealName = getIntent().getStringExtra("title")
        mealTitle.setText(mealName);
        mealkcal.setText(getIntent().getStringExtra("cal") + " kcal");
        Glide.with(this)
            .load(getIntent().getStringExtra("image"))
            .into(imageView)

        //get recipe ingredients and instructions list to display to user.
        val db = FirebaseFirestore.getInstance()
        db.collection("meals")
            .whereEqualTo("title", mealName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    docID = (document.id)
                    ingredients = (document.get("ingredients") as List<String>)
                    instructions = (document.get("instructions") as List<String>)
                    var i: Int = 0
                    for (element in ingredients){
                        i++
                        mealText = mealText + ("\u2022 \t") + element + "\n"
                    }
                    mealIngredients.setText(mealText)

                    i=0
                    mealText = ""
                    for (element in instructions){
                        i++
                        mealText = mealText + ("\u2022 \t") + element + "\n"
                    }
                    mealInstructions.setText(mealText)
                    //mealIngredients.setText(document.get("ingredients").toString())
                    //Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        //Perform Implicit Intent on click of webButton to launch the webpage.
        //webpage url is stored in database
        val websiteButton=findViewById<Button>(R.id.webButton);
        websiteButton.setOnClickListener{
            db.collection("meals")
                .whereEqualTo("title", mealName)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        docID = (document.id)
                        //website url retrieved from database to parse into the Implicit intent.
                        url = (document.get("url")).toString()

                        //Implicit Intent to view webpage to view recipe video using url from database
                        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }

        //on click of recipe button, explicit intent to Meal Suggestion Activity
        val recipeButton=findViewById<Button>(R.id.receipeButton);
        recipeButton.setOnClickListener{
            val intent = Intent(this,MealSuggestionActivity::class.java)
            startActivity(intent)
        }
    }
}

