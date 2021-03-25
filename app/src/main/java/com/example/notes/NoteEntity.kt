package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    var urgencyLevel: Int = 0
) {
    fun toDto() = Note(
        id = id,
        title = title,
        content = content,
        urgencyLevel = urgencyLevel
    )

    companion object {
        fun fromDto(dto: Note) =
            NoteEntity(
                id = dto.id,
                title = dto.title,
                content = dto.content,
                urgencyLevel = dto.urgencyLevel
            )
    }
}