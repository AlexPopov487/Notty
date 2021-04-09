package com.example.notes.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.Note
import com.example.notes.R
import com.example.notes.db.AppDb
import com.example.notes.repository.NoteRepositoryInMemory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepositoryInMemory(AppDb.getInstance(application).noteDao())

    private val _editedNote = MutableLiveData(Note())
    val editedNote: LiveData<Note>
        get() = _editedNote

    private val _isEmptyNoteAdded = MutableLiveData<Boolean>(false)
    val isEmptyNoteAdded: LiveData<Boolean>
        get() = _isEmptyNoteAdded
    fun resetEmptyNoteAdded() {
        _isEmptyNoteAdded.value = false
    }

    //TODO make this a coroutine function
    val noteData = repository.getAllNotes()

    fun saveNote() {
        editedNote.value?.let {
            if (it.content.isBlank() && it.title.isBlank()){
                _isEmptyNoteAdded.value = true
                return@let
            }
            saveNoteToDb(it)
        }
        _editedNote.value = Note()
    }

    private fun saveNoteToDb(note : Note) {
        viewModelScope.launch{
            // perform a potentially long-running db operation in coroutines
            repository.onSave(note)
        }
    }

    fun getNoteById(noteId: Long): Note {
        //TODO make this a coroutine function
             return repository.getNoteById(noteId)
    }



    fun removeNote(note: Note) {
        viewModelScope.launch{
            repository.removeNote(note)
        }
    }

    fun onNoteEdit(note: Note) {
        _editedNote.value = note
    }

    fun changeNoteContent(title: String, content: String, urgency: Int) {
        _editedNote.value?.let { note ->
            // if a note remained unchanged
            if (
                note.title == title &&
                note.content == content &&
                note.urgencyLevel == urgency
            ) {
                return
            } else {
                _editedNote.value = note.copy(title = title, content = content, urgencyLevel = urgency)
            }
        }
    }
}
