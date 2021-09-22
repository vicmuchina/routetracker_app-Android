package com.example.routetrackerapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.rangeTo
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_location.*
import java.lang.Float.parseFloat
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {



    var specific : String? = "null"
    private lateinit var latlng: ArrayList<String>
    private lateinit var times: ArrayList<String>
    private lateinit var userInIteration: ArrayList<String>



    val longitudeList: MutableList<Double> = mutableListOf()
    val latitudeList: MutableList<Double> = mutableListOf()
//    val timeStampList: MutableList<Double> = mutableListOf()


    val modelList: MutableList<LocationDetailsModel> = mutableListOf()






    private lateinit var mMap: GoogleMap
    val PERMISSION_ID = 10001
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{

        const val REQUEST_CHECK_SETTINGS = 43
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //declare an instance of the bundle class
        var bundle :Bundle ?=intent.extras
        specific = bundle?.getString("thisUser")
        times = bundle?.getStringArrayList("time") as   ArrayList<String>
        userInIteration = bundle?.getStringArrayList("userInIteration") as   ArrayList<String>


        latlng = bundle.getStringArrayList("coordinates") as ArrayList<String>

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //initializing the location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
        mMap = googleMap

        if (isPermissionGiven()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true

            if(specific == null){
                getCurrentLocation()
            }else{

//                val databaseReferencePlotting = FirebaseDatabase.getInstance()
//                        .getReference("LocationsTable")
//
//                //read our data
//
//                databaseReferencePlotting.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//
//
//                            Log.d("ggg", "Snapshot looks like : " + snapshot)
//                            //clear list
//
//                            //for loop to iterate of existing data
//                            for (h in snapshot.children) {
//                                Log.d("snapshotChildren", "Snapshot looks like : " + snapshot.children)
//                                //add to model class
//                                val users = h.getValue(LocationDetailsModel::class.java)
//
//                                modelList.add(users!!)
//
//
//
//                            }
//
//
//                        }
//
//
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(applicationContext,
//                                "Something went wrong, try again, Check internet connection"
//                                        + error.details, Toast.LENGTH_LONG).show()
//                        Log.d("error", "error is " + error.details)
//                    }
//
//                })

                if (modelList.isEmpty() == true){
                    val x = true
                    Log.d("xbm", "$x its empty $specific")
                    Log.d("xbc", "$latlng")
                    Log.d("xbd", "$userInIteration")



                }



                for (x in 0..(latlng.size-1)){


                    if(userInIteration[x] == specific) {
                        var latlong = latlng[x].split(',')
                        var lat = parseFloat(latlong[0]).toDouble()
                        var long = parseFloat(latlong[1]).toDouble()


                        latitudeList.add(lat)
                        longitudeList.add(long)

                    }


                }



                fun  bitmapDescriptorFromVector(context: Context,vectorResId:Int): BitmapDescriptor {
                    var vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
                    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
                    var bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    var canvas =  Canvas(bitmap)
                    vectorDrawable.draw(canvas)
                    return BitmapDescriptorFactory.fromBitmap(bitmap)
                }


//                for(x in 0..modelList.size){
//
//
//                    if (modelList[x].user == "vicmuchina1234@gmail.com"){
//
//                        latitudeList.add(modelList[x].latitude)
//                        longitudeList.add(modelList[x].longitude)
//                        timeStampList.add(modelList[x].timeStamp)
//                    }
//
//                }
//
                for(iteration in 0..(latitudeList.size-1)){

                    val markerLatitude = latitudeList[iteration].toDouble()
                    val markerLongitude = longitudeList[iteration].toDouble()
                    val time = times[iteration]
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(markerLatitude, markerLongitude))
                            .snippet("$specific at $time " + getCityName(markerLatitude,markerLongitude).toString() )
                            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_baseline_location_on_24))
                            .title("Current Location")
                    )
                    //camera position
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(markerLatitude, markerLongitude))
                        .zoom(500f)
                        .build()
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                }


//
//
////                plotUsersHistory(locationsList,timeStampList, specific!!)
            }


        } else {
            requestPermission()
        }
        //        // Add a marker in Sydney and move the camera
        //        val sydney = LatLng(-34.0, 151.0)
        //        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



    }

    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }
    private fun requestPermission() {

        ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)

    }

    private fun getCurrentLocation(){
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1500
        locationRequest.fastestInterval = 1000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        //turn on location
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)

        result.addOnCompleteListener {
            try{

                val response = it.getResult(ApiException::class.java)

                if(response!!.locationSettingsStates.isLocationPresent){
                    getLastLocation()
                }

            }catch(exception : ApiException){

                when(exception.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)

                    }catch(e: IntentSender.SendIntentException){

                        e.printStackTrace()

                    }catch (e: ClassCastException){
                        e.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }
            }

        }




    }

    fun getLastLocation() {

        if(checkPermission()) {

            if (isLocationEnabled()){

                fusedLocationProviderClient.lastLocation
                    .addOnCompleteListener {
                        //picking result
                        var location : Location? = it.result

                        if (location == null){

                            newLocationData()

                        }else{

                            fun  bitmapDescriptorFromVector(context: Context,vectorResId:Int): BitmapDescriptor {
                                var vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
                                vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
                                var bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                                var canvas =  Canvas(bitmap)
                                vectorDrawable.draw(canvas)
                                return BitmapDescriptorFactory.fromBitmap(bitmap)
                            }
                            //marker
//                    val icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.ic_baseline_location_on_24))
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(location.latitude, location.longitude))
                                    .snippet(getCityName(location.latitude,location.longitude).toString())
                                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_baseline_location_on_24))
                                    .title("Current Location")
                            )


                            //camera position
                            val cameraPosition = CameraPosition.Builder()
                                .target(LatLng(location.latitude, location.longitude))
                                .zoom(500f)
                                .build()
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                        }

                        if (location != null) {
                            Location().saveData(location.longitude, location.latitude)
                        }
                            Log.d("Debug", "your location" + location?.longitude + location?.latitude)

                        }

                    }

            else{
                Toast.makeText(applicationContext,"Turn your device location on", Toast.LENGTH_LONG).show()
            }

        }else{

            requestPermission()
        }

    }

    private fun getCityName(latitude: Double, longitude: Double): Any? {
        var cityName: String = ""
        var countryName = ""
        var fullAddress = ""

        //initialize GeoCoder reference
        var geoCoder = Geocoder(this@MapsActivity, Locale.getDefault())

        //full address
        var address = geoCoder.getFromLocation(latitude, longitude, 10)

        cityName = address.get(0).locality
        countryName = address.get(0).countryName
        fullAddress = address?.get(0)?.getAddressLine(0)!!

        Toast.makeText(applicationContext, "Full adress" + fullAddress, Toast.LENGTH_LONG).show()

        return fullAddress    }

    private fun newLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )

    }
    private var locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {

            var lastlocation: Location = locationResult.lastLocation

            Log.d("debug", "your last location: " + lastlocation.longitude.toString() + lastlocation.latitude.toString())


            Location().saveData(lastlocation.longitude, lastlocation.latitude)
            myLocationTextView.text = "Your location is: lon : " + lastlocation.longitude + " ,lat: " + lastlocation.latitude +
                    "\n" + getCityName(lastlocation.latitude, lastlocation.longitude)
        }

    }

    private fun checkPermission(): Boolean {

        //true if permission is active,false if not

        if(ActivityCompat.checkSelfPermission(this@MapsActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this@MapsActivity,android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            return true

        }
        return false
    }
    private fun isLocationEnabled(): Boolean {
        //state of the location services
        //if gps or network provider is enabled then return if not false

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CHECK_SETTINGS -> {

                if(resultCode == Activity.RESULT_OK){
                    getCurrentLocation()
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun plotUsersHistory(locationsList: MutableList<LatLng>,timeStampList: MutableList<String> , specificUser: String ){



        specific = null.toString()


    }







}



