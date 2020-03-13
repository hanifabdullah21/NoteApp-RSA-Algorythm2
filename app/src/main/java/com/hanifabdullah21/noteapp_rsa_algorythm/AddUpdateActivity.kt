package com.hanifabdullah21.noteapp_rsa_algorythm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_update.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateActivity : AppCompatActivity() {

    var noteDatabase: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update)

        noteDatabase = NoteDatabase.getInstance(this)

        val flag = intent.getStringExtra("FLAG")
        val note = intent.getParcelableExtra<NoteModel?>("NOTE")

        checkFlag(flag, note)

        aua_btn_save.onClick {
            if (flag=="ADD"){
                insertNewNote()
            }else{
                updateNote(note?.id)
            }
        }
    }

    private fun insertNewNote() {
        val cipher = doEncrypt()
        val note = NoteModel(
            title = aua_edt_title.text.toString(),
            message = cipher,
            createdAt = aua_tv_date.text.toString()
        )

        GlobalScope.launch {
            noteDatabase?.noteDao()?.insertNote(note)
            runOnUiThread {
                toast("Data berhasil ditambahkan")
            }
        }
    }

    private fun updateNote(id: Long?) {
        val cipher = doEncrypt()
        GlobalScope.launch {
            noteDatabase?.noteDao()?.updateNote(aua_edt_title.text.toString(), cipher.toString(), id!!)
            runOnUiThread {
                toast("Berhasil Update Note")
                finishAffinity()
                startActivity(intentFor<MainActivity>())
            }
        }
    }


    private fun doEncrypt(): String? {
        val RSA = RSA()
        val keyEncrypt = RSA.eValue(RSA.Qn)
        Log.d("ENCRYPT", "E VALUE $keyEncrypt")

        val plainteks = aua_edt_note.text.toString()

        var cipherTeks = ""
        for (element in plainteks) {
            val cipher = RSA.encrypt(element, keyEncrypt, RSA.n)
            cipherTeks += cipher
        }

        Log.d("Encryption", cipherTeks)
        return cipherTeks
    }

    private fun generateDate() {
        //Atur format waktu lokal (Indonesia)
        val locale = Locale("in", "ID")
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy , hh:mm:ss", locale)
        //Ambil waktu saat ini
        val date = Calendar.getInstance().time
        //Parsing waktu kedalam format lokal
        val createdDate = sdf.format(date).toString()
        //Atur ke dalam TextView
        aua_tv_date.text = createdDate
    }

    private fun checkFlag(flag: String?, note: NoteModel?) {
        if (flag=="ADD"){
            generateDate()
        }else{
            aua_tv_date.text = note?.createdAt

            aua_edt_title.setText(note?.title)
            val RSA = RSA()
            val keyEncrypt = RSA.eValue(RSA.Qn)
            val keyDecrypt = RSA.dValue(RSA.Qn, keyEncrypt)

            var plainTeks = ""
            for (element in note?.message!!) {
                val plain = RSA.decrypt(element, keyDecrypt, RSA.n)
                plainTeks += plain
            }
            aua_edt_note.setText(plainTeks)
        }
    }

}
