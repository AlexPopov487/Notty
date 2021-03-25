package com.example.notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.Note
import com.example.notes.R
import com.example.notes.databinding.NoteItemBinding

class NoteAdapter(private val interactionListeners: InteractionListeners) :
    ListAdapter<Note, NoteViewHolder>(DiffItemCallback) {
    companion object DiffItemCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), interactionListeners = interactionListeners
        )
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNoteItem = getItem(position)
        holder.bind(currentNoteItem)
    }
}

class NoteViewHolder(
    private val noteItemBinding: NoteItemBinding,
    private val interactionListeners: InteractionListeners
) : RecyclerView.ViewHolder(noteItemBinding.root) {
    fun bind(note: Note) {
        with(noteItemBinding) {

            when (note.urgencyLevel) {
                1 -> noteItemView.setBackgroundResource(R.color.notUrgent)
                2 -> noteItemView.setBackgroundResource(R.color.urgent)
                3 -> noteItemView.setBackgroundResource(R.color.veryUrgent)
                else -> noteItemView.setBackgroundColor(Color.TRANSPARENT)
            }

            if (note.title.isNotBlank()) {
                titleTv.text = note.title
                titleTv.visibility = View.VISIBLE
            } else {
                titleTv.visibility = View.GONE
            }
            titleTv.text = note.title
            contentTv.text = note.content

            noteItemCv.setOnClickListener {
                interactionListeners.onNoteClicked(note)
            }


        }
    }
}
