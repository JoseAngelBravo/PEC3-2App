package edu.uoc.android.rest

import edu.uoc.android.rest.models.Element
import edu.uoc.android.rest.models.Museums
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

  interface MuseumService {
//@GET("/api/dataset/museus/format/json/pag-ini/{pagIni}/pag-fi/{pagFi}")
@GET("/api/dataset/museus/format/json/")
fun museums(@Query("artist") artist: String): Call<Museums>


/*// @GET("/api/dataset/museus/format/json/pag-ini/{pagIni}/pag-fi/{pagFi}")
@GET("/api/dataset/museus/format/json/")
fun id(@Query("id") id: String): Call<Museums>*/
    }
