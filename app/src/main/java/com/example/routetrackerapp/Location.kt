package com.example.routetrackerapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_location.*
import java.text.SimpleDateFormat
import java.util.*

class Location : AppCompatActivity() {

    val mainHandler = Handler(Looper.getMainLooper())
    var counter :Int = 0


    val PERMISSION_ID = 10001
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest




    // tags for db
    private lateinit var databaseRef: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)



        auth = FirebaseAuth.getInstance()

        databaseRef = FirebaseDatabase.getInstance().getReference("LocationsTable")

        //initializing the location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
       locationButton.setOnClickListener {

            Log.d("debug", checkPermission().toString())
            Log.d("debug" , isLocationEnabled().toString())

           requestPermission()


           mainHandler.post(object : Runnable {

               override fun run() {
                   getLastLocation(counter)
                   mainHandler.postDelayed(this, 30000)
               }
           })




           Toast.makeText(this,"your location is currently being tracked",Toast.LENGTH_SHORT).show()

        }

        locationButtonTextView.setOnClickListener{

            Log.d("debug", checkPermission().toString())
            Log.d("debug" , isLocationEnabled().toString())

            requestPermission()


            mainHandler.post(object : Runnable {

                override fun run() {
                    getLastLocation(counter)
                    mainHandler.postDelayed(this, 80000)
                }
            })


            Toast.makeText(this,"your location is currently being tracked",Toast.LENGTH_SHORT).show()
        }
        signOut.setOnClickListener {

            auth.signOut()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
        stopTracking.setOnClickListener {

            this.counter = 100000000
            mainHandler.removeCallbacksAndMessages(null)
            Toast.makeText(this,"Your location has stopped being tracked",Toast.LENGTH_SHORT).show()
            this.counter = 0
        }

        googleMapsLink.setOnClickListener {

            val intent = Intent(applicationContext,MapsActivity::class.java)
            startActivity(intent)
        }
    }



    fun getLastLocation(counter: Int) {
        var iteration = counter

        if(checkPermission()) {

            if (isLocationEnabled()){

                fusedLocationProviderClient.lastLocation
                        .addOnCompleteListener {
                            //picking result
                            var location : Location? = it.result

                            if (location == null){
                                if (auth.currentUser == null){
                                    val intent = Intent(applicationContext,Login::class.java)
                                    startActivity(intent)
                                    Toast.makeText(applicationContext,"You need to login to start tracking your location", Toast.LENGTH_SHORT).show()
                                }
                                newLocationData()
                                iteration += 1
                                this.counter = iteration

                                if (counter == 100000000){
                                    mainHandler.removeCallbacksAndMessages(null)
                                    this.counter = 0
                                }

                            }else{
                                if (auth.currentUser == null){
                                    val intent = Intent(applicationContext,Login::class.java)
                                    startActivity(intent)
                                    Toast.makeText(applicationContext,"You need to login to start tracking your location", Toast.LENGTH_SHORT).show()
                                }


                                    saveData(location.longitude, location.latitude)

                                    Log.d(
                                        "Debug",
                                        "your location" + location.longitude + location.latitude
                                    )
                                    myLocationTextView.text =
                                        "Your location is: lon : " + location.longitude + " ,lat: " + location.latitude +
                                                "\n" + getCityName(
                                            location.latitude,
                                            location.longitude
                                        )
                                iteration += 1
                                this.counter = iteration

                                if (counter == 100000000){
                                    mainHandler.removeCallbacksAndMessages(null)
                                    this.counter = 0
                                }
                            }

                        }

            }else{
                Toast.makeText(applicationContext,"Turn your device location on", Toast.LENGTH_SHORT).show()
            }

        }else{

            requestPermission()
        }


    }



    fun saveData(longitude: Double, latitude: Double) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val auth = FirebaseAuth.getInstance().currentUser
        var usersName = auth?.email
//        val hexUser = usersName?.let { ASCIItoHEX().toHex(it) }



        databaseRef = FirebaseDatabase.getInstance().getReference("LocationsTable")
        //beginning the process of taking data to realtime database
        //generate unique id for records
        val locationId = databaseRef.push().key
        //hold this data in our model
        val locationUpload = locationId.let {
           LocationDetailsModel(locationId.toString(),usersName.toString(),longitude.toString(),latitude.toString(), currentDate.toString())
        }
        //send values to realtime database
        if (locationId != null){
            databaseRef.child(locationId).setValue(locationUpload)
                ?.addOnCompleteListener {

                }.addOnFailureListener{
                    Toast.makeText(
                        applicationContext,
                        "Error, check internet settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }  //end of sending text data to real time database

        else{
            // Handle failures //e.g //
            Toast.makeText(
                    applicationContext,
                    " Error occurred , check internet connection",
                    Toast.LENGTH_LONG
            ).show()

        }


    }

    private fun isLocationEnabled(): Boolean {
        //state of the location services
        //if gps or network provider is enabled then return if not false

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission(): Boolean {

        //true if permission is active,false if not

        if(ActivityCompat.checkSelfPermission(this@Location,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this@Location,android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            return true

        }
        return false
    }


    private fun requestPermission() {

        ActivityCompat.requestPermissions(this@Location, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)

    }

    private fun newLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1500
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

            saveData(lastlocation.longitude, lastlocation.latitude)
            myLocationTextView.text = "Your location is: lon : " + lastlocation.longitude + " ,lat: " + lastlocation.latitude +
                    "\n" + getCityName(lastlocation.latitude, lastlocation.longitude)
        }

    }



        //check permission for location from user


        private fun getCityName(latitude : Double, longitude :Double): String {

            var cityName: String = ""
            var countryName = ""
            var fullAddress = ""

            //initialize GeoCoder reference
            var geoCoder = Geocoder(this@Location, Locale.getDefault())

            //full address
            var address = geoCoder.getFromLocation(latitude, longitude, 10)

            cityName = address.get(0).locality
            countryName = address.get(0).countryName
            fullAddress = address?.get(0)?.getAddressLine(0)!!

            Toast.makeText(applicationContext, "Full adress" + fullAddress, Toast.LENGTH_LONG).show()

            return fullAddress


        }



 }


