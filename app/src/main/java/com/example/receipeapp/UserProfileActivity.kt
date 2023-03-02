package com.example.receipeapp

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val bmi = findViewById<TextView>(R.id.userBMI)
        val weightCategory = findViewById<TextView>(R.id.userWeightCategory)
        val name = findViewById<TextView>(R.id.userName)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        //get user information from Firestore cloud database.
        db = FirebaseFirestore.getInstance()
        //get user information from "users" collection in firestore,
        // specifically the document that has the current user id.
        db.collection("users").document(uid.toString())
            .get()
            .addOnSuccessListener {
                //extract only the value from the "bmi" field
                bmi.setText("BMI value:\t"+(it.get("bmi")).toString())
                //extract only the value from the "bmiCategory" field
                weightCategory.setText("Weight:\t"+(it.get("bmiCategory")).toString())
                //extract only the value from the "name" field
                name.setText("Name:\t"+(it.get("name")).toString())

                //if user is Underweight or Obese, change text color to RED.
                if(((it.get("bmiCategory")).toString()).equals("Underweight",true) ||
                    ((it.get("bmiCategory")).toString()).equals("Obese",true)){
                    weightCategory.setTextColor(Color.parseColor("#FF5349"))
                }
                //if user is Overweight, change text color to ORANGE.
                else if(((it.get("bmiCategory")).toString()).equals("Overweight",true)){
                    weightCategory.setTextColor(Color.parseColor("#FF9636"))
                }
                else{
                    //if user is of Normal Weight, change text color to GREEN.
                    weightCategory.setTextColor(Color.parseColor("#5FD85F"))
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                //error exception handling, displays error message if any failure in receiving information from database
            }

        //on click of profile picture icon, run pickPhoto() method
        val imageView = findViewById<ImageView>(R.id.imageView2)
        imageView.setOnClickListener{
            pickPhoto(imageView)
        }

        //on click of done button, user is redirected back to meal suggestion activity, using Explicit Intent.
        val doneButton=findViewById<Button>(R.id.userDoneButton);
        doneButton.setOnClickListener{
            val intent = Intent(this,MealSuggestionActivity::class.java)
            startActivity(intent)
        }

    }

    fun pickPhoto(view: View){
        //if user has not been requested permission yet, set request code to 1 (meaning must seek permission)
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) { //
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        }
        //if permission is completed, set request code to 2
        else {
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }

    //if permission not requested yet, run this method
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            //request permission first
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //intent to directly to go access the gallery, if permission granted by user
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //If permission already granted by user before, can directly access gallery to get image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val imageView = findViewById<ImageView>(R.id.imageView2)

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            //photo picked by user from gallery
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                //Load/Replace the image in imageView
                Glide.with(this@UserProfileActivity)
                    .load(pickedPhoto)
                    .into(imageView)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}