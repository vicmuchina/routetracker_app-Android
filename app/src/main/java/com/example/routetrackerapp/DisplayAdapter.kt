package com.example.routetrackerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DisplayAdapter(
    private val context: Context,
    private val peoplesList: List<LocationDetailsModel>,private val coordinates: ArrayList<String>,private val submissionTime: ArrayList<String>,private val usersPerIteration: ArrayList<String>
) : RecyclerView.Adapter<DisplayAdapter.LocationDetailsViewHolder>() ,CallbackInterface {

    val userLocationList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationDetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.display_item, parent, false)
        return LocationDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationDetailsViewHolder, position: Int) {


        val lastUpdated = peoplesList[position].timeStamp
        val specificUser = peoplesList[position]?.user

        holder.lastupdated.text = "Last updated: " + lastUpdated
        holder.userEmail.text = specificUser
        holder.seeInMaps.setOnClickListener {

            if (specificUser != null) {

                val intent = Intent(context, MapsActivity::class.java)
                intent.putExtra("thisUser",specificUser)
                intent.putStringArrayListExtra("coordinates", coordinates)
                intent.putStringArrayListExtra("userInIteration", usersPerIteration)
                intent.putStringArrayListExtra("time", submissionTime)

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;


                context.startActivity(intent)

            }
        }

        holder.delete.setOnClickListener {
            if (specificUser != null) {
                deleteRecordOfUserFromFirebase(specificUser)
            }
        }

    }

    private fun deleteRecordOfUserFromFirebase(specificUser: String) {
        //create ref to the db
        val auth = FirebaseAuth.getInstance().currentUser
        var usersName = auth?.email

        if (usersName != specificUser){
            Toast.makeText(
                context,
                "You can only delete your own tracking record",
                Toast.LENGTH_LONG
            ).show()

        }else {

            val databaseReference = FirebaseDatabase.getInstance()
                    .getReference("LocationsTable")

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
                            val deletingUser = users?.user
                            val deletingId = users?.id
                            //add details to list
                            if (deletingUser == specificUser) {
                                val databaseReferenceDeleting = FirebaseDatabase.getInstance()
                                    .getReference("LocationsTable").child(deletingId!!)

                                databaseReferenceDeleting.removeValue()
                            }

                            Toast.makeText(
                                context,
                                "Delete of tracking record successful",
                                Toast.LENGTH_LONG
                            ).show()

                            Log.d("userList", " snapshot is " + userLocationList)

                        }

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Something went wrong, try again, Check internet connection"
                                + error.details, Toast.LENGTH_LONG
                    ).show()
                    Log.d("error", "error is " + error.details)
                }

            })








        }


    }

    override fun getItemCount(): Int = peoplesList.size


    class LocationDetailsViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {

        val userEmail = itemView.findViewById<TextView>(R.id.userEmail)
        val lastupdated = itemView.findViewById<TextView>(R.id.last_updated)
        val seeInMaps = itemView.findViewById<TextView>(R.id.see_route_in_googleMaps)
        val delete = itemView.findViewById<Button>(R.id.DeleteButton)
    }

    override fun passResultCallback(message: String) {

    }
}