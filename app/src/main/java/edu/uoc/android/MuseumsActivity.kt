package edu.uoc.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.android.rest.RetrofitFactory
import edu.uoc.android.rest.DataMuseums
import edu.uoc.android.rest.MuseumService
import edu.uoc.android.rest.models.Museums

import kotlinx.android.synthetic.main.activity_museums.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MuseumsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_museums)




        val progressBar: ProgressBar = this.indeterminateBar

// task is run on a thread
        Thread(Runnable {
            // display the  progressbar
            this@MuseumsActivity.runOnUiThread(java.lang.Runnable {
                progressBar.visibility = View.VISIBLE
            })


                 DataMuseums.Download()


            // when the task is completed, make progressBar gone, and do stuff
            this@MuseumsActivity.runOnUiThread(java.lang.Runnable {
                llenarDatos()
                progressBar.visibility = View.GONE
            })
        }).start()




    }

fun llenarDatos(){
    viewManager = LinearLayoutManager(this)
    viewAdapter =   RecyclerAdapter(DataMuseums.elements)

    recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        setHasFixedSize(true)

        // use a linear layout manager
        layoutManager = viewManager

        // specify an viewAdapter (see also next example)
        adapter = viewAdapter

    }
}


}