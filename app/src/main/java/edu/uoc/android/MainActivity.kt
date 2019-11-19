package edu.uoc.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rellay_museums.setOnClickListener {
            val intent = Intent(
                this,
                MuseumsActivity::class.java
            ) //create intent to call the second activity
            startActivity(intent)// start the second activity
        }

        rellay_maps.setOnClickListener {
            val intent =
                Intent(this, MapsActivity::class.java) //create intent to call the second activity
            startActivity(intent)// start the second activity
        }

    }
}