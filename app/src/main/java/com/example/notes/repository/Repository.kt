package com.example.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.Note

interface Repository {
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun onSave(note: Note)
    fun getNoteById(noteId: Long) : Note
    suspend fun removeNote(note: Note)
    suspend fun removeAll()

    fun getByUrgency(urgency: Int): LiveData<List<Note>>
//    fun getUrgent(): LiveData<List<Note>>
//    fun getVeryUrgent(): LiveData<List<Note>>

}