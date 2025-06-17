package com.uti.pm_project

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class InputTargetActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var bulan: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_target)

        val tvBulan = findViewById<TextView>(R.id.tvBulan)
        val etTarget = findViewById<EditText>(R.id.etTarget)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val rvRiwayat = findViewById<RecyclerView>(R.id.rvRiwayat)

        val bulanDariIntent = intent.getStringExtra("bulan") ?: "Januari"
        val tahun = Calendar.getInstance().get(Calendar.YEAR)
        bulan = "$bulanDariIntent $tahun"

        tvBulan.text = "Target Bulan $bulan"

        sharedPref = getSharedPreferences("TargetTabungan", Context.MODE_PRIVATE)
        val savedTarget = sharedPref.getInt(bulan, 0)
        if (savedTarget != 0) {
            etTarget.setText(savedTarget.toString())
        }

        btnSimpan.setOnClickListener {
            val input = etTarget.text.toString()
            if (input.isNotEmpty()) {
                val target = input.toInt()
                sharedPref.edit().putInt(bulan, target).apply()
                Toast.makeText(this, "Target Bulan $bulan disimpan", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Mohon masukkan nominal", Toast.LENGTH_SHORT).show()
            }
        }

        val db = DatabaseTabungan(this)
        val transaksiList = db.getTransaksiByBulan(bulan)
        rvRiwayat.layoutManager = LinearLayoutManager(this)
        rvRiwayat.adapter = RiwayatAdapter(transaksiList)
    }
}
