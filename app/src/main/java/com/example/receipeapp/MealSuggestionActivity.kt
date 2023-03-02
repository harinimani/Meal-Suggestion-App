package com.example.receipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MealSuggestionActivity : AppCompatActivity() {

    private lateinit var mealArrayList : ArrayList<Meal>
    private lateinit var mealAdapter : MealAdapter
    private lateinit var recyclerView: RecyclerView
    private var db = Firebase.firestore
    private lateinit var query:Query

    //Categories View
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_suggestion)

        recyclerView = findViewById(R.id.mealSuggester)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)

        mealArrayList = arrayListOf()

        //Initialize Categories
        initCategory()

        //fetch meal data from Database
        fetchData()

        categoryAdapter.onItemClick = { category ->
            // do something with your item
            Log.d("TAG", category.categoryName)
            Toast.makeText(this, category.categoryName, Toast.LENGTH_SHORT).show()
            categoryData(category.categoryName)
        }

        //user profile activity
        val userProfile = findViewById<ImageView>(R.id.userProfileIcon)
        userProfile.setOnClickListener{
            val intent = Intent(this,UserProfileActivity::class.java)
            startActivity(intent)
        }

    }

    //fetchData() method to fetch recipe datas from the Firestore database
    private fun fetchData() {
        db = FirebaseFirestore.getInstance()
        //gets data from Firestore, meals collection and sorts in ascending order of meal title
        db.collection("meals").orderBy("title", Query.Direction.ASCENDING)
            .get().addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val food : Meal? = data.toObject(Meal::class.java)
                        if (food != null){
                            mealArrayList.add(food)     //stores the recipe data in arraylist
                        }
                    }
                    //fills recycler view with arraylist items
                    recyclerView.adapter = MealAdapter(this,mealArrayList)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun categoryData(selectedCategory:String){
        mealArrayList = arrayListOf()
        db = FirebaseFirestore.getInstance()

        // Create a reference to the cities collection
        val categoryDb = db.collection("meals")
        query = db.collection("meals").orderBy("title", Query.Direction.ASCENDING)



        if (selectedCategory.equals("Dairy",true))
            query = categoryDb.whereEqualTo("category.dairy", true)
        else if (selectedCategory.equals("Vegetables",true))
            query = categoryDb.whereEqualTo("category.veg", true)
        else if (selectedCategory.equals("Meat",true))
            query = categoryDb.whereEqualTo("category.meat", true)
        else if (selectedCategory.equals("Seafood",true))
            query = categoryDb.whereEqualTo("category.seafood", true)
        else if (selectedCategory.equals("Whole\n"+"grains",true))
            query = categoryDb.whereEqualTo("category.grain", true)
        else if (selectedCategory.equals("Nuts &\n"+"Seeds",true))
            query = categoryDb.whereEqualTo("category.nut", true)
        else
            query = db.collection("meals").orderBy("title", Query.Direction.ASCENDING)

        mealArrayList = arrayListOf()
        query.get().addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val food : Meal? = data.toObject(Meal::class.java)
                        if (food != null){
                            mealArrayList.add(food)
                        }
                    }
                    recyclerView.adapter = MealAdapter(this,mealArrayList)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }


    private fun initCategory() {
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
        categoryRecyclerView.setHasFixedSize(true)
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        categoryList = ArrayList()

        addCategory()

        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter

    }


    private fun addCategory() {
        categoryList.add(Category(R.drawable.all_ingredients_icon, "All"))
        categoryList.add(Category(R.drawable.grain, "Whole\ngrains"))
        categoryList.add(Category(R.drawable.steak, "Meat"))
        categoryList.add(Category(R.drawable.seafood, "Seafood"))
        categoryList.add(Category(R.drawable.veg, "Vegetables"))
        categoryList.add(Category(R.drawable.fruit, "Fruits"))
        categoryList.add(Category(R.drawable.dairy, "Dairy"))
        categoryList.add(Category(R.drawable.nut, "Nuts &\nSeeds"))
    }



}