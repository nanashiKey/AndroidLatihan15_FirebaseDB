package com.ngopidev.project.androidlatihan15_firebasedb.all_activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ngopidev.project.androidlatihan15_firebasedb.helper.PrefsHelper
import com.ngopidev.project.androidlatihan15_firebasedb.R
import com.ngopidev.project.androidlatihan15_firebasedb.adapter.BukuAdapter
import com.ngopidev.project.androidlatihan15_firebasedb.model.BukuModel
import kotlinx.android.synthetic.main.halaman_depan.*


/**
 *   created by Irfan Assidiq on 2019-07-02
 *   email : assidiq.irfan@gmail.com
 **/
class HalamanDepan : AppCompatActivity() , BukuAdapter.FirebaseDataListener{
    private var bukuAdapter : BukuAdapter? = null

    private var rcView : RecyclerView? = null

    private var list : MutableList<BukuModel>? = null
    lateinit var dbref : DatabaseReference
    lateinit var helperPrefs : PrefsHelper
    private lateinit var fAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.halaman_depan)
        helperPrefs = PrefsHelper(this)
        rcView = findViewById(R.id.rc_view)
        rcView!!.layoutManager = LinearLayoutManager(applicationContext)
        rcView!!.setHasFixedSize(true)
        fAuth = FirebaseAuth.getInstance()

        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<BukuModel>()
                for(dataSnapshot in p0.children){
                        val addDataAll = dataSnapshot.getValue(BukuModel::class.java)
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list!!.add(addDataAll!!)
                        bukuAdapter = BukuAdapter(
                            this@HalamanDepan,
                            list!!
                        )
                        rcView!!.adapter = bukuAdapter

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                e("TAGERROR", p0.message)
            }

        })

        fab_.setOnClickListener {
            //let'do something
            startActivity(Intent(this, Tambah_Data::class.java))
        }
        upload_storage.setOnClickListener {
            startActivity(Intent(this, UploadFireStorage::class.java))
        }
     }

    override fun onDeletedData(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        if(dbref != null){
            dbref.child(buku.getKey()).removeValue().addOnSuccessListener {
                Toast.makeText(this, "data berhasil dihapus",
                    Toast.LENGTH_SHORT).show()
                bukuAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onUpdated(buku: BukuModel, position: Int) {
        dbref = FirebaseDatabase.getInstance()
            .getReference("dataBuku/${helperPrefs.getUID()}")
        if(dbref != null){
            val datax = dbref.child(buku.getKey()).key
            val intent = Intent(this, Tambah_Data::class.java)
            intent.putExtra("kode", datax)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ctx_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.logout -> {
                fAuth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
