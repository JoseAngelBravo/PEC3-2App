package edu.uoc.android.rest
import android.util.Log
import edu.uoc.android.rest.models.Element
import edu.uoc.android.rest.models.Museums
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataMuseums {
    companion object {
        var elements: List<Element>? = null


        fun Download() {
            var done = false

            if (DataMuseums.elements == null) {
                val retrofitfactory = RetrofitFactory
                val call = retrofitfactory.museumService.museums("")
                call.enqueue(object : Callback<Museums> {
                    override fun onResponse(call: Call<Museums>, response: Response<Museums>) {
                        if (response.code() == 200) {

                            //perform data reception process
                            val museums = response.body()!!
                            DataMuseums.elements = museums.getElements()

                            Log.i("Datos", "Datos de museos descargados correctamente")
                            done=true
                        }
                    }

                    override fun onFailure(call: Call<Museums>, t: Throwable) {
                        Log.e("Datos", "Error al descargar los datos ")
                        done=true
                    }
                })
            } else {
                Log.i("Datos", "Datos ya habían sido descargados")
                done=true
            }
            while (!done) {
                //wait to the process to end
            }
        }


     }
}
