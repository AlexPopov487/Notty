package com.example.notes.adapter

import com.example.notes.Note

interface InteractionListeners {
    fun onNoteClicked(note: Note)
}