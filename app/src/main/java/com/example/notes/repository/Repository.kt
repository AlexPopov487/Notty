package com.example.notes.repository

import androidx.lifecycle.LiveData
import com.example.notes.Note

interface Repository {
    fun getAllNotes(): LiveData<List<Note>>
    fun onSave(note: Note)
    fun getUrgencyLevel(noteId: Long) : LiveData<Int>
    fun changeUrgency(noteId: Long, urgencyIndex: Int)
    fun getNoteById(noteId: Long) : Note
    fun removeNote(note: Note)
}