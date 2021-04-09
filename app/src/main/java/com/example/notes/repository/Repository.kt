package com.example.notes.repository

import androidx.lifecycle.LiveData
import com.example.notes.Note

interface Repository {
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun onSave(note: Note)
    fun getNoteById(noteId: Long) : Note
    suspend fun removeNote(note: Note)
}