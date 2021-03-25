package com.example.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.notes.Note
import com.example.notes.NoteEntity
import com.example.notes.dao.NoteDao

class NoteRepositoryInMemory(private val noteDao: NoteDao) : Repository {

    override fun getAllNotes() = Transformations.map(noteDao.getAllNotes()) {entityList ->
        entityList.map {
            it.toDto()
        }
    }



    override fun getNoteById(noteId: Long): Note {
        return noteDao.getNoteById(noteId).toDto()
    }

    override fun getUrgencyLevel(noteId: Long): LiveData<Int> {
        val currentNote = noteDao.getNoteById(noteId).toDto()
        return MutableLiveData(currentNote.urgencyLevel)
    }

    override fun changeUrgency(noteId: Long, urgencyIndex: Int) {
        val currentNote = noteDao.getNoteById(noteId).toDto()
        currentNote.urgencyLevel = urgencyIndex
    }

    override fun removeNote(note: Note) {
        noteDao.remove(NoteEntity.fromDto(note))
    }

    override fun onSave(note: Note) {
        noteDao.onSave(NoteEntity.fromDto(note))
    }
}