package com.uti.pm_project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class cuan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuan)

        val backButton = findViewById<ImageView>(R.id.btnKembali)
        backButton.setOnClickListener {
            val intent = Intent(this, utama::class.java)
            startActivity(intent)
            finish()
        }
    }
}
