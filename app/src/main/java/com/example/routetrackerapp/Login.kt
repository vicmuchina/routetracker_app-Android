package com.example.routetrackerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    //call the FirebaseAuth class
    private lateinit var auth: FirebaseAuth
    //variables to store users input
    var emailLogin: String = ""
    var passLogin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //creating an instance of the firebase auth class
        auth = FirebaseAuth.getInstance()

        textView5.setOnClickListener {

            val intent = Intent(applicationContext,ForgotPassword::class.java)

            startActivity(intent)
        }

        create_account.setOnClickListener {
            val intent = Intent(applicationContext,CreateAccount::class.java)

            startActivity(intent)

        }
        appCompatButton2.setOnClickListener {

            captureInput()

        }

    }

    private fun captureInput() {
        emailLogin = editTextEmail.text.toString().trim()
        passLogin = editTextPassword.text.toString()

        //validation of inputs
        if (emailLogin.isEmpty() && passLogin.isEmpty()){
            Toast.makeText(applicationContext,
                "Fields cannot be empty", Toast.LENGTH_LONG).show()
        } else {
            loginUser(emailLogin,passLogin)
        }
    }

    private fun loginUser(emailLogin: String, passLogin: String) {

        auth.signInWithEmailAndPassword(emailLogin,passLogin)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext,
                        "Login verified", Toast.LENGTH_SHORT).show()
//                    createUser()
                    updateUi()
                }else {
                    Toast.makeText(applicationContext,
                        "Login Failure " + it.exception , Toast.LENGTH_LONG).show()
                    Log.d("auth","details are " + it.exception)
                }
            }
    }

//    private fun createUser() {
//
//        val auth = FirebaseAuth.getInstance().currentUser
//        var usersName = auth?.email
//
//        var userList: MutableList<UsersModel> = mutableListOf()
//
//         lateinit var databaseRefUser: DatabaseReference
//
//
//        databaseRefUser = FirebaseDatabase.getInstance().getReference("usersTable")
//        //read our data
//        databaseRefUser!!.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot!!.exists()){
//                    Log.d("userSnap","Snapshot looks like : " + snapshot)
//                    //clear list
//                    userList.clear()
//                    //for loop to iterate of existing data\
//                    for (h in snapshot.children){
//                        //add to model class
//                        val user = h.getValue(UsersModel::class.java)
//                        //add details to list
//                        userList?.add(user!!)
//                        Log.d("shoeSnap", " snapshot is " + user)
//                    }
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext,
//                        "Something went wrong, try again, Check internet connection"
//                                +  error.details, Toast.LENGTH_LONG).show()
//                Log.d("error","error is " + error.details)
//            }
//
//        })
//
//        var checker = 0
//        if (userList.size != 0) {
//            for (user in (0..userList.size)) {
//
//                val currentUser = userList[user].user
//
//                if (currentUser == usersName) {
//                    checker = 1
//                }
//
//            }
//        }
//
//
//        if (checker == 0){
//
//            val userId = databaseRefUser.push().key
//
//            val userUpload = userId.let {
//                if (usersName != null) {
//                    UsersModel(usersName)
//                }
//            }
//
//            //send values to realtime database
//            if (userId != null){
//                databaseRefUser.child("1").setValue(userUpload)
//                        ?.addOnCompleteListener {
//
//                        }.addOnFailureListener{
//                            Toast.makeText(
//                                    applicationContext,
//                                    "Error, check internet settings",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//            }  //end of sending text data to real time database
//
//            else{
//                // Handle failures //e.g //
//                Toast.makeText(
//                        applicationContext,
//                        " Error occurred , check internet connection",
//                        Toast.LENGTH_LONG
//                ).show()
//
//            }
//
//
//
//
//        }
//
//
//
//
//
//    }


    private fun updateUi() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }



}