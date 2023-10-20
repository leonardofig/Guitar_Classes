package com.example.guitarclasses.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.guitarclasses.database.DBHelper
import com.example.guitarclasses.databinding.ActivityUserRegisterBinding

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            val db = DBHelper(this)
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            if (username.trim().equals("") || password.trim().equals("")) {
                Toast.makeText(
                    this,
                    "Enter username and password to continue!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val userCount = db.getUserCount()
                if (userCount >= 3) {
                    // Show a message that the limit of users has been reached
                    Toast.makeText(
                        this,
                        "User limit has been reached",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (db.registar(username, password) > 0) {
                        Toast.makeText(this, "Registration created successfully!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error creating record", Toast.LENGTH_SHORT).show()
                        binding.editUsername.setText("")
                        binding.editPassword.setText("")
                    }
                }
            }
        }
    }
}