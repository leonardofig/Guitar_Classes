package com.example.guitarclasses.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.guitarclasses.WelcomeActivity
import com.example.guitarclasses.database.DBHelper
import com.example.guitarclasses.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val db = DBHelper(this)

        val id = i.getIntExtra("id", 0)
        val user = db.select(id)

        if (user != null) {
            binding.editUsername.setText(user.username)
            binding.editPassword.setText(user.password)
        }

        binding.buttonEditar.setOnClickListener {
            val res = db.mudarPassword(
                id, binding.editUsername.text.toString(), binding.editPassword.text.toString()
            )
            if (res > 0) {
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error when changing password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDeleteAccount.setOnClickListener {
            val res = db.eliminarConta(id)
            if (res > 0) {
                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error deleting account", Toast.LENGTH_SHORT).show()

            }
        }
        binding.buttonContinue.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

    }
}