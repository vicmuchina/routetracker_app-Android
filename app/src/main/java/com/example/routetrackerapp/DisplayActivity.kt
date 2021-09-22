package com.example.routetrackerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display.*

class DisplayActivity : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    //mutable list to store our fetch from firebase

    lateinit var userLocationList: MutableList<LocationDetailsModel>
    var coordinates:ArrayList<String> = ArrayList()
    var time: ArrayList<String> = ArrayList()
    var userList: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        //settings recyclerview
        recyclerLocations.layoutManager = LinearLayoutManager(this)
        recyclerLocations.setHasFixedSize(true)


        //intialise db ref
        databaseReference = FirebaseDatabase.getInstance().getReference("LocationsTable")

        //initialize our mutable list
        userLocationList = mutableListOf()


        //read our data

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {


                    Log.d("specificUserSnap", "Snapshot looks like : " + snapshot)
                    //clear list
                    userLocationList.clear()
                    //for loop to iterate of existing data
                    for (h in snapshot.children) {
                        //add to model class
                        val users = h.getValue(LocationDetailsModel::class.java)
                        //add details to list
                         userLocationList?.add(users!!)
                        var latitude = users!!.latitude
                        var longitude = users!!.longitude
                        var timestamp = users!!.timeStamp
                        var userNow = users!!.user

                        var latLng = "$latitude,$longitude"
                        coordinates.add(latLng)
                        time.add(timestamp)
                        userList.add(userNow!!)

                        Log.d("userList", " snapshot is " + userLocationList)

                    }

                }


                val adapter = DisplayAdapter(applicationContext,userLocationList,coordinates,time,userList)
                //setting the adapter for the recyclerView
                recyclerLocations?.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,
                        "Something went wrong, try again, Check internet connection"
                                + error.details, Toast.LENGTH_LONG).show()
                Log.d("error", "error is " + error.details)
            }

        })





    }

}

