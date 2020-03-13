package com.hanifabdullah21.noteapp_rsa_algorythm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    var noteDatabase: NoteDatabase? = null

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteDatabase = NoteDatabase.getInstance(this)

        layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(ArrayList())
        ma_rv_note.layoutManager = layoutManager
        ma_rv_note.adapter = adapter

        getAllNotes()

        val path = getDatabasePath("NoteApp.db").canonicalPath
        Log.d("DATABASE", "Path Database $path")

        ma_fab.onClick {
            startActivity(intentFor<AddUpdateActivity>("FLAG" to "ADD"))
        }
    }

    private fun getAllNotes() {
        GlobalScope.launch {
            val list: List<NoteModel>? = noteDatabase?.noteDao()?.getAllNote()
            runOnUiThread {
                adapter.setListOfNote(list)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllNotes()
    }
}
