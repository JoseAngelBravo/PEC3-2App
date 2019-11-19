package edu.uoc.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import edu.uoc.android.rest.DataMuseums
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback , GoogleMap.OnMarkerClickListener{
    override fun onMarkerClick(p0: Marker?) = false



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    //lateint: only creates when is necessary
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var lastLocation: Location //to store last known location


    //To receive location updates
    // 1
    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//To receive location updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude),"última localización")
            }
        }
        createLocationRequest()



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()

        Thread(Runnable {
            DataMuseums.Download() //try to download museums data if needed


            // when the task is completed
            this@MapsActivity.runOnUiThread(java.lang.Runnable {
                val museumsIterator = DataMuseums.elements?.iterator()

                museumsIterator!!.forEach {
                    val ltln= it.localitzacio.split(",")
                    val lat=ltln[0].toDouble()
                    val ln=ltln[1].toDouble()
                    val location =  LatLng(lat,ln)
                    placeMarkerOnMap(location,it.adrecaNom)
                }
            })

        }).start()

    }
    private fun setUpMap() {

        //permissions checking
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                // placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }

        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        //GoogleMap.MAP_TYPE_NORMAL
        //GoogleMap.MAP_TYPE_SATELLITE
        //GoogleMap.MAP_TYPE_HYBRID

    }

    //to create markers
    private fun placeMarkerOnMap(location: LatLng, title: String) {


        val markerOptions = MarkerOptions().position(location)
        if (title!="última localización"){
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.ic_user_location)))
        }
        markerOptions.title(title)
        map.addMarker(markerOptions)
    }

    //to show adress at markers
    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                    fun getAddress(latLng: LatLng): String {
                        // 1
                        val geocoder = Geocoder(this)
                        val addresses: List<Address>?
                        val address: Address?
                        var addressText = ""

                        try {
                            // 2
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            // 3
                            if (null != addresses && !addresses.isEmpty()) {
                                address = addresses[0]
                                for (i in 0 until address.maxAddressLineIndex) {
                                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                                }
                            }
                        } catch (e: IOException) {
                            Log.e("MapsActivity", e.localizedMessage)
                        }

                        return addressText
                    }    }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }


    //To receive location updates
    private fun startLocationUpdates() {
        //Control of granted GPS permissions, if not, ask for it
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }
    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
}
