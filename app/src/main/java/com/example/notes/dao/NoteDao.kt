package com.example.notes.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.notes.Note

@Dao
interface NoteDao {

    //Note: Since you’re returning a LiveData object, there’s no need to use suspend on this method.
    // In fact, Room won’t even allow it. The LiveData object relies on the observer pattern where
    // the caller can subscribe to changes on the value it contains. Whenever new data are available
    // from the database, this list will update and reflect that data within the UI. It won’t need
    // to re-query the database.
    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE urgencyLevel = :urgency")
    fun getByUrgency(urgency: Int): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun onSave(note: Note)

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getNoteById(id: Long): Note

    @Delete
    suspend fun remove(note: Note)

    @Query("DELETE FROM Note")
    suspend fun removeAll()
}