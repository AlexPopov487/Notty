package com.example.notes.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.notes.Note
import com.example.notes.db.AppDb
import com.example.notes.repository.NoteRepositoryInMemory
import kotlinx.coroutines.launch

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

    private val _completeNoteList = MutableLiveData<List<Note>>()
    val completeNoteList: LiveData<List<Note>>
        get() = _completeNoteList


    //TODO make this a coroutine function
    // Note: Since you’re returning a LiveData object, there’s no need to use suspend on this method.
    //    In fact, Room won’t even allow it. The LiveData object relies on the observer pattern where
    //    the caller can subscribe to changes on the value it contains. Whenever new data are available
    //    from the database, this list will update and reflect that data within the UI. It won’t need
    //    to re-query the database.
    val noteData = repository.getAllNotes()


    fun setNoteData() {
        _completeNoteList.value = noteData.value
    }


    fun saveNote() {
        editedNote.value?.let {
            if (it.content.isBlank() && it.title.isBlank()) {
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
             return repository.getNoteById(noteId)
    }

    fun removeNote(note: Note) {
        viewModelScope.launch{
            repository.removeNote(note)
        }
    }

    fun removeAll(){
        viewModelScope.launch{
            repository.removeAll()
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

    fun filterByUrgency(urgency: Int, isFilterChecked: Boolean) {
        val unfilteredList = noteData.value!!
        if (isFilterChecked) {
            _completeNoteList.value = noteData.value?.filter { it.urgencyLevel == urgency }
        } else {
            _completeNoteList.value = unfilteredList
        }
    }

}
