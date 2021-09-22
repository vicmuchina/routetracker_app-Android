package com.example.routetrackerapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)


        val compassImage = view.findViewById<ImageView>(R.id.compass)
        val userImage = view.findViewById<ImageView>(R.id.usersRoute)
        val myLocationImage = view.findViewById<ImageView>(R.id.roundLocationIcon)
        val compassTextView = view.findViewById<TextView>(R.id.compassTextView)
        val userRouteTextView = view.findViewById<TextView>(R.id.userRouteTextView)
        val myLocationTextView = view.findViewById<TextView>(R.id.myLocationTextView)


        userImage.setOnClickListener {

            val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if(user!=null) {
                val intent = Intent(context, DisplayActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
            }


        }

        userRouteTextView.setOnClickListener {

            val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if(user!=null) {
                val intent = Intent(context, DisplayActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
            }


        }


        myLocationImage.setOnClickListener {

            val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if(user!=null) {
                val intent = Intent(context, Location::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
            }

        }
        myLocationTextView.setOnClickListener {

            val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if(user!=null) {
                val intent = Intent(context, Location::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(context, Login::class.java)
                startActivity(intent)
            }

        }
        compassImage.setOnClickListener {

            val intent = Intent(context,Compass::class.java)

            startActivity(intent)
        }
        compassTextView.setOnClickListener {

            val intent = Intent(context,Compass::class.java)

            startActivity(intent)
        }

        return view
    }


}