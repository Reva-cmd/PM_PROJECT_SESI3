package com.uti.pm_project

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class kelola : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelola)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bulanButtons = mapOf(
            R.id.jan to "Januari",
            R.id.feb to "Februari",
            R.id.mar to "Maret",
            R.id.apr to "April",
            R.id.mei to "Mei",
            R.id.juni to "Juni",
            R.id.juli to "Juli",
            R.id.agus to "Agustus",
            R.id.sep to "September",
            R.id.okto to "Oktober",
            R.id.nov to "November",
            R.id.des to "Desember"
        )

        for ((id, bulan) in bulanButtons) {
            findViewById<Button>(id).setOnClickListener {
                val intent = Intent(this, InputTargetActivity::class.java)
                intent.putExtra("bulan", bulan)
                startActivity(intent)
            }
        }

        val btnResetTarget = findViewById<Button>(R.id.btnReset)
        btnResetTarget.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reset Semua Data")
                .setMessage("Apakah kamu yakin ingin menghapus semua target tabungan dan riwayat transaksi?")
                .setPositiveButton("Ya") { _, _ ->
                    val prefs = getSharedPreferences("TargetTabungan", Context.MODE_PRIVATE)
                    prefs.edit().clear().apply()

                    val db = DatabaseTabungan(this)
                    db.hapusSemuaTransaksi()

                    Toast.makeText(this, "Semua target dan riwayat berhasil direset", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
        }


        val btnKembali = findViewById<ImageView>(R.id.btnKembali)
        btnKembali.setOnClickListener {
            val intent = Intent(this, utama::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
