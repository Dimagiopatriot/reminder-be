package com.reminder.infrastructure.mysql

import com.reminder.core.model.Status
import com.reminder.core.model.note.Note
import com.reminder.core.model.note.NoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Repository
class NoteMySqlRepository : NoteRepository {

    @Autowired
    private lateinit var noteDaoRepository: NoteDaoRepository

    override fun getNotes(date: Date, userId: Long): List<Note> {
        val formattedDateString = SimpleDateFormat("yyyy-MM-dd").format(date)
        val localDateTime = LocalDate.parse(formattedDateString)
        val startTimestamp = localDateTime.atTime(0, 0, 0, 0)
            .toInstant(ZoneOffset.UTC).toEpochMilli()
        val endTimestamp = localDateTime.atTime(23, 59, 59, 0)
            .toInstant(ZoneOffset.UTC).toEpochMilli()

        val notesDto = noteDaoRepository.findNotesBetweenTimestamps(startTimestamp, endTimestamp, userId)
        return notesDto.map {
            Note(
                id = it.id,
                timestamp = it.timestamp!!,
                noteName = it.noteName!!,
                description = it.description,
                status = Status.valueOf(it.status!!)
            )
        }
    }

    override fun saveNote(note: Note, userId: Long): Note {
        val noteDao = noteDaoRepository.save(
            NoteDao(
                note.id,
                note.timestamp,
                note.noteName,
                note.description,
                note.status.name,
                userId
            )
        )
        return Note(
            id = noteDao.id,
            timestamp = noteDao.timestamp!!,
            noteName = noteDao.noteName!!,
            description = noteDao.description,
            status = Status.valueOf(noteDao.status!!.toUpperCase())
        )
    }

    override fun deleteNote(id: Long) {
        noteDaoRepository.deleteById(id)
    }

}

@Repository
interface NoteDaoRepository : JpaRepository<NoteDao, Long> {
    @Query(value = "select * from `note` where timestamp between ?1 and ?2 and user_id = ?3", nativeQuery = true)
    fun findNotesBetweenTimestamps(startTimestamp: Long,
                                   endTimestamp: Long,
                                   userId: Long
    ): List<NoteDao>
}

@Table(name = "note")
@Entity
data class NoteDao(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val timestamp: Long? = null,
    val noteName: String? = null,
    val description: String? = null,
    val status: String? = null,
    val userId: Long? = null
) : Serializable