package com.uti.pm_project

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ambil_tabungan : AppCompatActivity() {

    private lateinit var etTanggal: EditText
    private lateinit var etJumlah: EditText
    private lateinit var etCatatan: EditText
    private lateinit var btnSimpan: Button

    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ambil_tabungan)

        etTanggal = findViewById(R.id.etTanggal)
        etJumlah = findViewById(R.id.etJumlah)
        etCatatan = findViewById(R.id.etCatatan)
        btnSimpan = findViewById(R.id.btnSimpan)

        val calendar = Calendar.getInstance()

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        etTanggal.setOnClickListener {
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time

                    val sdfTanggal = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                    etTanggal.setText(sdfTanggal.format(selectedDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSimpan.setOnClickListener {
            val jumlahText = etJumlah.text.toString()
            val catatan = etCatatan.text.toString()

            if (selectedDate == null || jumlahText.isEmpty()) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jumlah = jumlahText.toIntOrNull()
            if (jumlah == null || jumlah <= 0) {
                Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("BulanDipilih", MODE_PRIVATE)
            val bulanDipilih = prefs.getString("bulan", "") ?: ""


            val sdfTanggal = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val tanggalFormatted = sdfTanggal.format(selectedDate!!)

            val db = DatabaseTabungan(this)
            val sukses = db.simpanTransaksi(
                bulan = bulanDipilih,
                jumlah = jumlah,
                keterangan = catatan,
                tipe = "Ambil",
                tanggal = tanggalFormatted
            )

            if (sukses) {
                Toast.makeText(this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
