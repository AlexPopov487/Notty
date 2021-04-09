package com.example.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.Note
import com.example.notes.dao.NoteDao

class NoteRepositoryInMemory(private val noteDao: NoteDao) : Repository {

    override fun getAllNotes() = noteDao.getAllNotes()


    override fun getNoteById(noteId: Long): Note =  noteDao.getNoteById(noteId)


//    override fun getUrgencyLevel(noteId: Long): LiveData<Int> {
//        val currentNote = noteDao.getNoteById(noteId)
//        return MutableLiveData(currentNote.urgencyLevel)
//    }
//
//    override fun changeUrgency(noteId: Long, urgencyIndex: Int) {
//        val currentNote = noteDao.getNoteById(noteId)
//        currentNote.urgencyLevel = urgencyIndex
//    }

    override suspend fun removeNote(note: Note) {
        noteDao.remove(note)
    }

    override suspend fun onSave(note: Note) {
        noteDao.onSave(note)
    }
}