package com.example.routetrackerapp

import com.google.firebase.database.ServerValue

class LocationDetailsModel(
        val id: String,
        val user: String?, val longitude: String,
        val latitude: String, var timeStamp: String ){
//    //blank constructor for reading process
    constructor() : this("", "", "", "" , "" )



}