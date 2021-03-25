package com.example.notes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes.Note
import com.example.notes.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun onSave(note: NoteEntity)

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getNoteById(id: Long): NoteEntity

    @Delete
    fun remove(note: NoteEntity)
}