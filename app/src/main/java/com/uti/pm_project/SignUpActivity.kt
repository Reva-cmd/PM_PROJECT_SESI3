package com.uti.pm_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val edtUsername = findViewById<EditText>(R.id.edtNewUsername)
        val edtPassword = findViewById<EditText>(R.id.edtNewPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.putString("savedUsername", username)
                editor.putString("savedPassword", password)
                editor.apply()

                Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, login2Activity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
