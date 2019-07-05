package com.ngopidev.project.androidlatihan15_firebasedb.model

import java.io.Serializable


/**
 *   created by Irfan Assidiq on 2019-07-02
 *   email : assidiq.irfan@gmail.com
 **/
class BukuModel{
    private var nama : String? = null
    private var tanggal : String? = null
    private var judulBuku : String? = null
    private var id : String? = null
    private var description : String? = null
    private var key : String? = null // untuk menyimpan key value dari firebase db

    constructor(){}
    constructor(nama : String, tanggal : String, judul : String){
        this.nama = nama
        this.tanggal = tanggal
        this.judulBuku = judul
    }
    constructor(nama : String, tanggal : String, judul : String, description: String){
        this.nama = nama
        this.tanggal = tanggal
        this.judulBuku = judul
        this.description = description
    }

    fun getNama() : String{ return nama!! }
    fun getTanggal() : String{ return tanggal!! }
    fun getJudulBuku() : String{ return judulBuku!! }
    fun getId(): String{return  id!!}
    fun setId(id : String){this.id = id}
    fun getDescription() : String{return description!!}
    fun setDesc(desc : String){this.description = desc}
    fun setNama(nama : String){this.nama = nama}
    fun setTanggal(tanggal: String){this.tanggal = tanggal}
    fun setJudul(judul : String){this.judulBuku = judul}
    fun getKey() : String{return key!!}
    fun setKey(key : String){this.key = key}
}