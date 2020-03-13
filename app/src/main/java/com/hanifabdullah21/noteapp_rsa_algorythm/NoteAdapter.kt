package com.hanifabdullah21.noteapp_rsa_algorythm

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class NoteAdapter (var list: List<NoteModel>?) : RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.in_tv_date.text = list?.get(position)?.createdAt
            itemView.in_tv_title.text = list?.get(position)?.title
            itemView.in_tv_note.text = list?.get(position)?.message

            itemView.onClick {
                itemView.context.startActivity(itemView.context.intentFor<DetailActivity>("NOTE" to list?.get(position)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setListOfNote(list: List<NoteModel>?){
        this.list = list
        notifyDataSetChanged()
    }
}