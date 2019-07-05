package com.ngopidev.project.androidlatihan15_firebasedb.all_activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.e
import android.widget.Toast
import com.google.firebase.database.*
import com.ngopidev.project.androidlatihan15_firebasedb.helper.PrefsHelper
import com.ngopidev.project.androidlatihan15_firebasedb.R
import com.ngopidev.project.androidlatihan15_firebasedb.model.BukuModel
import kotlinx.android.synthetic.main.tambah_data.*


/**
 *   created by Irfan Assidiq on 2019-07-02
 *   email : assidiq.irfan@gmail.com
 **/
class Tambah_Data : AppCompatActivity(){

    lateinit var dbRef : DatabaseReference
    lateinit var helperPref : PrefsHelper
    var datax : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_data)

        datax = intent.getStringExtra("kode")
        helperPref = PrefsHelper(this)

        if(datax != null){
            showdataFromDB()
        }

        btn_simpan.setOnClickListener {
            val nama = et_namaPenulis.text.toString()
            val judul = et_judulBuku.text.toString()
            val tgl = et_tanggal.text.toString()
            val desc = et_description.text.toString()

            if(nama.isNotEmpty() || judul.isNotEmpty() || tgl.isNotEmpty() ||
                    desc.isNotEmpty()){
                simpanToFireBase(nama, judul, tgl, desc)
            }else{
                Toast.makeText(this, "inputan tidak boleh kosong",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun simpanToFireBase(nama: String, judul : String, tgl : String, desc : String){
        val uidUser = helperPref.getUID()
        if(datax == null){
        val counterID = helperPref.getCounterId()
        dbRef = FirebaseDatabase.getInstance().getReference("dataBuku/$uidUser")
            dbRef.child("$counterID/id").setValue(uidUser)
            dbRef.child("$counterID/nama").setValue(nama)
            dbRef.child("$counterID/judulBuku").setValue(judul)
            dbRef.child("$counterID/tanggal").setValue(tgl)
            dbRef.child("$counterID/description").setValue(desc)
            Toast.makeText(this, "Data Berhasil ditambahkan",
            Toast.LENGTH_SHORT).show()
            helperPref.saveCounterId(counterID+1)
        }else{
            dbRef = FirebaseDatabase.getInstance()
                .getReference("dataBuku/$uidUser/$datax")
            dbRef.child("id").setValue(uidUser)
            dbRef.child("nama").setValue(nama)
            dbRef.child("judulBuku").setValue(judul)
            dbRef.child("tanggal").setValue(tgl)
            dbRef.child("description").setValue(desc)
            Toast.makeText(this, "Data Berhasil ditambahkan",
                Toast.LENGTH_SHORT).show()
        }

        onBackPressed()
    }

    fun showdataFromDB(){
        dbRef = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPref.getUID()}/${datax}/")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val buku = p0.getValue(BukuModel::class.java)
                et_namaPenulis.setText(buku!!.getNama())
                et_judulBuku.setText(buku.getJudulBuku())
                et_tanggal.setText(buku.getTanggal())
                et_description.setText(buku.getDescription())
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

}