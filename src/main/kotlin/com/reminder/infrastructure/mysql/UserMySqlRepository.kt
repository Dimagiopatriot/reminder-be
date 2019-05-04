package com.reminder.infrastructure.mysql

import com.reminder.core.model.note.Note
import com.reminder.core.model.note.NoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
class UserMySqlRepository : NoteRepository {

    @Autowired
    lateinit var noteDtoRepository: NoteDtoRepository

    override fun getNotes(date: Date): List<Note> {
    }

    override fun updateNote(note: Note): Note {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteNote(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNote(note: Note): Note {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

@Repository
interface NoteDtoRepository : JpaRepository<NoteDto, Long>

@Table(name = "note")
@Entity
data class NoteDto(
    @Id
    @GeneratedValue
    val id: Long,
    val timestamp: Long,
    val noteName: String,
    val description: String,
    val status: String,

    val userId: Long
)