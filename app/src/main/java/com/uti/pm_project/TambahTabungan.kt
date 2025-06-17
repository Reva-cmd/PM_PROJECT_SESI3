package com.uti.pm_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class TambahTabungan : AppCompatActivity() {

    private lateinit var etJumlah: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_tabungan)

        etJumlah = findViewById(R.id.etJumlah)
        etKeterangan = findViewById(R.id.etKeterangan)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnBatal = findViewById(R.id.btnBatal)

        val prefs = getSharedPreferences("BulanDipilih", MODE_PRIVATE)
        val bulanDipilih = prefs.getString("bulan", "") ?: ""

        btnSimpan.setOnClickListener {
            val jumlahStr = etJumlah.text.toString()
            val keterangan = etKeterangan.text.toString()

            if (jumlahStr.isEmpty()) {
                Toast.makeText(this, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jumlah = jumlahStr.toIntOrNull() ?: 0
            if (jumlah <= 0) {
                Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = DatabaseTabungan(this)
            db.simpanTransaksi(
                bulan = bulanDipilih,
                tanggal = getCurrentDate(),
                jumlah = jumlah,
                keterangan = keterangan,
                tipe = "Tambah"
            )

            Toast.makeText(this, "Tabungan berhasil ditambahkan", Toast.LENGTH_SHORT).show()

            val intent = Intent()
            intent.putExtra("jumlah_tabungan", jumlah)
            intent.putExtra("keterangan", keterangan)
            setResult(RESULT_OK, intent)
            finish()
        }

        btnBatal.setOnClickListener {
            finish()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        return sdf.format(Date())
    }
}
