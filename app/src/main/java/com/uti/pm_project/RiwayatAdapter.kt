package com.uti.pm_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiwayatAdapter(private val riwayatList: List<TransaksiModel>) :
    RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvTipe: TextView = itemView.findViewById(R.id.tvTipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val item = riwayatList[position]
        holder.tvTanggal.text = item.tanggal
        holder.tvKeterangan.text = item.keterangan
        holder.tvJumlah.text = "Rp ${item.jumlah}"
        holder.tvTipe.text = "Tipe: ${item.tipe}"

        if (item.tipe == "Ambil") {
            holder.tvJumlah.setTextColor(0xFFFF0000.toInt())
        } else {
            holder.tvJumlah.setTextColor(0xFF4CAF50.toInt())
        }
    }

    override fun getItemCount(): Int = riwayatList.size
}
