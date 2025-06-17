package com.uti.pm_project

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class tabunganku : AppCompatActivity() {

    private lateinit var tvTarget: TextView
    private lateinit var tvPemasukan: TextView
    private lateinit var tvPengeluaran: TextView
    private lateinit var tvSelisih: TextView
    private lateinit var spinnerBulan: Spinner
    private lateinit var btnTambah: Button
    private lateinit var btnAmbil: Button

    private var target = 0
    private var pemasukan = 0
    private var pengeluaran = 0
    private var bulanAktif = ""

    private val bulanList = arrayOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabunganku)

        tvTarget = findViewById(R.id.tvTarget)
        tvPemasukan = findViewById(R.id.tvPemasukan)
        tvPengeluaran = findViewById(R.id.tvPengeluaran)
        tvSelisih = findViewById(R.id.tvSelisih)
        spinnerBulan = findViewById(R.id.spinnerBulan)
        btnTambah = findViewById(R.id.btnTambah)
        btnAmbil = findViewById(R.id.btnAmbil)
        val btnKembali = findViewById<ImageView>(R.id.btnKembali)

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            bulanList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.WHITE)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK)
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBulan.adapter = adapter

        btnKembali.setOnClickListener {
            val intent = Intent(this, utama::class.java)
            startActivity(intent)
            finish()
        }

        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val tahun = Calendar.getInstance().get(Calendar.YEAR)
                bulanAktif = "${bulanList[position]} $tahun"

                val prefs = getSharedPreferences("BulanDipilih", Context.MODE_PRIVATE)
                prefs.edit().putString("bulan", bulanAktif).apply()

                refreshData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnTambah.setOnClickListener {
            val intent = Intent(this, TambahTabungan::class.java)
            startActivity(intent)
        }

        btnAmbil.setOnClickListener {
            val intent = Intent(this, ambil_tabungan::class.java)
            startActivity(intent)
        }

        val bulanSekarang = Calendar.getInstance().get(Calendar.MONTH)
        spinnerBulan.setSelection(bulanSekarang)
    }

    override fun onResume() {
        super.onResume()
        if (bulanAktif.isNotEmpty()) {
            refreshData()
            DatabaseTabungan(this).logSemuaData()
        }
    }

    private fun refreshData() {
        loadTarget(bulanAktif)
        loadDataTransaksi(bulanAktif)
        updateUI()
    }

    private fun loadTarget(bulan: String) {
        val prefs = getSharedPreferences("TargetTabungan", Context.MODE_PRIVATE)
        target = prefs.getInt(bulan, 0)
    }

    private fun loadDataTransaksi(bulan: String) {
        val db = DatabaseTabungan(this)
        val transaksi = db.getTransaksiByBulan(bulan)

        // Log semua transaksi untuk debug
        for (t in transaksi) {
            android.util.Log.d("DEBUG_TRANSAKSI", "tipe=${t.tipe} | jumlah=${t.jumlah}")
        }

        // Hindari case sensitivity pada tipe
        pemasukan = transaksi.filter { it.tipe.equals("tambah", ignoreCase = true) }.sumOf { it.jumlah }
        pengeluaran = transaksi.filter { it.tipe.equals("ambil", ignoreCase = true) }.sumOf { it.jumlah }
    }

    private fun updateUI() {
        if (target == 0) {
            tvTarget.text = "Rp 0"
            tvPemasukan.text = "Total Pemasukan: Rp 0"
            tvPengeluaran.text = "Total Pengeluaran: Rp 0"
            tvSelisih.text = "Selisih: Rp 0"
            btnTambah.isEnabled = false
            btnAmbil.isEnabled = false
        } else {
            val saldo = pemasukan - pengeluaran
            tvTarget.text = "Rp $target"
            tvPemasukan.text = "Total Pemasukan: Rp $pemasukan"
            tvPengeluaran.text = "Total Pengeluaran: Rp $pengeluaran"
            tvSelisih.text = "Selisih: Rp ${saldo - target}"
            btnTambah.isEnabled = true
            btnAmbil.isEnabled = true
        }
    }
}
