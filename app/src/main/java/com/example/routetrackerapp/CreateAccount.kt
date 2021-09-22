package com.example.routetrackerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var email: String = ""
    var password: String = ""
    var conpass: String = ""
    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //getting instance of firebase auth product
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {

            val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
        forgotPass.setOnClickListener {

            val intent = Intent(applicationContext,ForgotPassword::class.java)

        }
        create.setOnClickListener {

            captureInput()

        }

    }

    private fun captureInput() {

        username = userName.text.toString().trim()
        email = editTextEmail.text.toString().trim()
        password = editTextPassword.text.toString()
        conpass = editTextConfirm.text.toString()

        //validation
        if (!password.equals(conpass) || email.isEmpty() || password.isEmpty()){
            Toast.makeText(applicationContext
                ,"Passwords do not match or Fields cannot be empty"
                , Toast.LENGTH_LONG).show()

        } else {
            registerToFirebase(email,password)
        }

    }

    private fun registerToFirebase(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext,
                        "Account created", Toast.LENGTH_LONG).show()
                    Log.d("auth","details are " + it.result)
                    updateUi()
                } else {
                    Toast.makeText(applicationContext
                        ,"Account not created, try again " + it.exception,
                        Toast.LENGTH_LONG).show()
                    Log.d("auth","details are " + it.exception)
                }
            }

    }

    private fun updateUi() {
        val intent = Intent(applicationContext,Login::class.java)
        startActivity(intent)
    }
}