package com.hanifabdullah21.noteapp_rsa_algorythm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_update.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity() {

    var noteDatabase: NoteDatabase? = null

    var isDecrypt = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        noteDatabase = NoteDatabase.getInstance(this)

        //Mendapatkan data yang dibawa saat berpindah ke halaman ini
        val note = intent.getParcelableExtra<NoteModel?>("NOTE")

        //Menampilkan data pada tampilan yang telah disediakan
        da_tv_date.text = note?.createdAt
        da_tv_title.text = note?.title
        da_tv_message.text = note?.message

        da_btn_dekrip.onClick {
            decryptMessage(note)
        }

        da_ib_delete.onClick {
            deleteNote(note)
        }

        da_ib_edit.onClick {
            startActivity(intentFor<AddUpdateActivity>("FLAG" to "UPDATE", "NOTE" to note))
        }
    }

    private fun decryptMessage(note: NoteModel?) {
        if(!isDecrypt){
            val RSA = RSA()
            val keyEncrypt = RSA.eValue(RSA.Qn)
            val keyDecrypt = RSA.dValue(RSA.Qn, keyEncrypt)

            var plainTeks = ""
            for (element in note?.message!!) {
                val character = element
                val plain = RSA.decrypt(character, keyDecrypt, RSA.n)
                plainTeks += plain
            }
            da_tv_message.text = plainTeks
            isDecrypt = true
            da_btn_dekrip.text = "Enkripsi"
        }else{
            da_tv_message.text = note?.message
            isDecrypt = false
            da_btn_dekrip.text = "Dekripsi"
        }
    }

    private fun deleteNote(note: NoteModel?) {
        GlobalScope.launch {
            noteDatabase?.noteDao()?.deleteNote(note!!)
            runOnUiThread {
                toast("Berhasil hapus note")
                this@DetailActivity.finish()
            }
        }
    }

}
