package com.example.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.Note
import com.example.notes.dao.NoteDao

class NoteRepositoryInMemory(private val noteDao: NoteDao) : Repository {

    override fun getAllNotes() = noteDao.getAllNotes()


    override fun getNoteById(noteId: Long): Note =  noteDao.getNoteById(noteId)

    override suspend fun removeAll() {
        noteDao.removeAll()
    }

    override fun getByUrgency(urgency: Int): LiveData<List<Note>> = noteDao.getByUrgency(urgency)

//    override fun getVeryUrgent(): LiveData<List<Note>> = noteDao.getVeryUrgent()
//
//    override fun getNotUrgent(): LiveData<List<Note>> = noteDao.getNotUrgent()

    override suspend fun removeNote(note: Note) {
        noteDao.remove(note)
    }

    override suspend fun onSave(note: Note) {
        noteDao.onSave(note)
    }
}