package com.example.routetrackerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() ,BottomNavigationView.OnNavigationItemSelectedListener {

    //initialize variable to refer to bottom navigation
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hide action bar
        supportActionBar?.hide()

        //load up a default fragment so that the view is not empty

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()

        bottomNav = findViewById(R.id.bottomView)

        bottomNav.setOnNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.myLocation -> {
                val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
                if(user!=null) {
                    val intent = Intent(applicationContext, Location::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                }

            }
            R.id.allUsersLocation -> {
                val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
                if(user!=null) {
                    val intent = Intent(applicationContext, DisplayActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                }

            }
            R.id.compass -> {
                val intent = Intent(applicationContext,Compass::class.java)

                startActivity(intent)

            }

        }

        return true
    }
}