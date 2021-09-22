package com.example.routetrackerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var emailReset: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        login_forgot_password.setOnClickListener {
            val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
        appCompatButton.setOnClickListener {

            emailReset = changePassEditText.text.toString()

            if (emailReset.isEmpty()){
                Toast.makeText(applicationContext,
                    "Email should not be empty", Toast.LENGTH_LONG).show()

            } else {
                resetFirebase(emailReset)
            }
        }


    }

    private fun resetFirebase(emailReset: String) {

        auth.sendPasswordResetEmail(emailReset)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext,
                        "Reset link sent to email,check inbox to reset"
                        , Toast.LENGTH_LONG).show()
                    Log.d("auth","details are " + it.result)
                    updateUi()
                } else {
                    Toast.makeText(applicationContext,
                        "something went wrong try again", Toast.LENGTH_LONG).show()
                    Log.d("auth","details are " + it.exception)
                }
            }

    }

    private fun updateUi() {
        val intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)
    }
}