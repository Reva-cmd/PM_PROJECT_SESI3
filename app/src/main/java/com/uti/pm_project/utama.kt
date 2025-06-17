package com.uti.pm_project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class utama : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)


        val profilButton = findViewById<ShapeableImageView>(R.id.lingkar)
        profilButton.setOnClickListener {
            val intent = Intent(this, Profil::class.java)
            startActivity(intent)
        }

        val kllabutton = findViewById<ImageView>(R.id.klla)
        kllabutton.setOnClickListener {
            val intent = Intent(this, kelola::class.java)
            startActivity(intent)
        }
        val tabunganbutton = findViewById<ImageView>(R.id.tabungan)
        tabunganbutton.setOnClickListener {
            val intent = Intent(this, tabunganku::class.java)
            startActivity(intent)
        }
        val tipsCuanButton = findViewById< ImageView>(R.id.TipsCuan)
        tipsCuanButton.setOnClickListener {
            val intent = Intent(this, cuan::class.java)
            startActivity(intent)
        }
    }
}
