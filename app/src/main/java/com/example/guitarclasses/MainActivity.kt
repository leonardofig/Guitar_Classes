package com.example.guitarclasses

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guitarclasses.account.AccountActivity
import com.example.guitarclasses.databinding.ActivityMainBinding
import com.example.guitarclasses.account.UserRegisterActivity
import com.example.guitarclasses.database.DBHelper


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogIn.setOnClickListener {
            val db = DBHelper(this)
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            if (username.trim().equals("") || password.trim().equals("")) {
                Toast.makeText(
                    this,
                    "Enter username and password to continue",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val res = db.login(username, password)
                if (res > 0) {
                    val i = Intent(this, AccountActivity::class.java)
                    i.putExtra("id", res)
                    startActivity(i)
                } else {
                    Toast.makeText(this, "Wrong login, try again", Toast.LENGTH_SHORT)
                        .show()
                }
//                binding.editUsername.setText("")
//                binding.editPassword.setText("")
            }
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, UserRegisterActivity::class.java))
        }
    }
}