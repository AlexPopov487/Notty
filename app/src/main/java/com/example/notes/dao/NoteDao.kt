package com.example.notes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun onSave(note: Note)

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getNoteById(id: Long): Note

    @Delete
    suspend fun remove(note: Note)
}