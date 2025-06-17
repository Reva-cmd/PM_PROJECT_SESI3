package com.uti.pm_project

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Profil : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNomor: EditText
    private lateinit var etTanggalLahir: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var imageProfile: ImageView

    private lateinit var sharedPreferences: android.content.SharedPreferences
    private val PICK_IMAGE = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        initViews()
        loadSavedData()
        setupListeners()
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNomor = findViewById(R.id.etNomor)
        etTanggalLahir = findViewById(R.id.etTanggalLahir)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        imageProfile = findViewById(R.id.imageProfile)
        sharedPreferences = getSharedPreferences("ProfilPrefs", Context.MODE_PRIVATE)
    }

    private fun loadSavedData() {
        etNama.setText(sharedPreferences.getString("nama", ""))
        etEmail.setText(sharedPreferences.getString("email", ""))
        etNomor.setText(sharedPreferences.getString("nomor", ""))
        etTanggalLahir.setText(sharedPreferences.getString("tanggal", ""))
        val savedGender = sharedPreferences.getString("gender", "")

        if (savedGender == "Laki-laki") radioGroupGender.check(R.id.radioLaki)
        else if (savedGender == "Perempuan") radioGroupGender.check(R.id.radioPerempuan)

        val imageUriString = sharedPreferences.getString("profileImageUri", null)
        if (!imageUriString.isNullOrEmpty()) {
            imageProfile.setImageURI(Uri.parse(imageUriString))
        }
    }

    private fun setupListeners() {
        etTanggalLahir.setOnClickListener {
            showDatePickerDialog()
        }

        findViewById<Button>(R.id.btnGantiFoto).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE)
        }

        findViewById<Button>(R.id.btnSimpan).setOnClickListener {
            saveProfileData()
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            etTanggalLahir.setText("$d/${m + 1}/$y")
        }, year, month, day).show()
    }

    private fun saveProfileData() {
        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val nomor = etNomor.text.toString().trim()
        val tanggal = etTanggalLahir.text.toString().trim()
        val gender = when (radioGroupGender.checkedRadioButtonId) {
            R.id.radioLaki -> "Laki-laki"
            R.id.radioPerempuan -> "Perempuan"
            else -> ""
        }

        if (nama.isEmpty() || email.isEmpty() || nomor.isEmpty() || tanggal.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString("nama", nama)
        editor.putString("email", email)
        editor.putString("nomor", nomor)
        editor.putString("tanggal", tanggal)
        editor.putString("gender", gender)
        selectedImageUri?.let {
            editor.putString("profileImageUri", it.toString())
        }
        editor.apply()

        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageProfile.setImageURI(selectedImageUri)
        }
    }
}
