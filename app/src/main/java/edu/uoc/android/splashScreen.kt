package edu.uoc.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class splashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Start Activity
        val intent = Intent(this, MainActivity::class.java) //create intent to call the second activity
        startActivity(intent) //call the second activity


        finish()//close splash activity
    }
}
