package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

/**
 * PUBLIC_INTERFACE
 * Repository implementing CRUD over a local SQLite database for notes.
 */
class NotesRepository(context: Context) {
    private val dbHelper = NotesDbHelper(context.applicationContext)

    // PUBLIC_INTERFACE
    fun getAllNotes(): List<Note> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            NotesDbHelper.TABLE_NOTES,
            arrayOf(
                NotesDbHelper.COLUMN_ID,
                NotesDbHelper.COLUMN_TITLE,
                NotesDbHelper.COLUMN_CONTENT,
                NotesDbHelper.COLUMN_UPDATED_AT
            ),
            null,
            null,
            null,
            null,
            "${NotesDbHelper.COLUMN_UPDATED_AT} DESC"
        )
        return cursor.use { c -> readNotesFromCursor(c) }
    }

    // PUBLIC_INTERFACE
    fun searchNotes(query: String): List<Note> {
        val db = dbHelper.readableDatabase
        val like = "%${query.replace("%", "\\%").replace("_", "\\_")}%"
        val cursor = db.query(
            NotesDbHelper.TABLE_NOTES,
            arrayOf(
                NotesDbHelper.COLUMN_ID,
                NotesDbHelper.COLUMN_TITLE,
                NotesDbHelper.COLUMN_CONTENT,
                NotesDbHelper.COLUMN_UPDATED_AT
            ),
            "${NotesDbHelper.COLUMN_TITLE} LIKE ? ESCAPE '\\' OR ${NotesDbHelper.COLUMN_CONTENT} LIKE ? ESCAPE '\\'",
            arrayOf(like, like),
            null,
            null,
            "${NotesDbHelper.COLUMN_UPDATED_AT} DESC"
        )
        return cursor.use { c -> readNotesFromCursor(c) }
    }

    // PUBLIC_INTERFACE
    fun getNoteById(id: Long): Note? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            NotesDbHelper.TABLE_NOTES,
            arrayOf(
                NotesDbHelper.COLUMN_ID,
                NotesDbHelper.COLUMN_TITLE,
                NotesDbHelper.COLUMN_CONTENT,
                NotesDbHelper.COLUMN_UPDATED_AT
            ),
            "${NotesDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use { c ->
            return if (c.moveToFirst()) {
                noteFromCursor(c)
            } else null
        }
    }

    // PUBLIC_INTERFACE
    fun insertNote(title: String, content: String): Long {
        val values = ContentValues().apply {
            put(NotesDbHelper.COLUMN_TITLE, title)
            put(NotesDbHelper.COLUMN_CONTENT, content)
            put(NotesDbHelper.COLUMN_UPDATED_AT, System.currentTimeMillis())
        }
        val db = dbHelper.writableDatabase
        return db.insert(NotesDbHelper.TABLE_NOTES, null, values)
    }

    // PUBLIC_INTERFACE
    fun updateNote(id: Long, title: String, content: String): Int {
        val values = ContentValues().apply {
            put(NotesDbHelper.COLUMN_TITLE, title)
            put(NotesDbHelper.COLUMN_CONTENT, content)
            put(NotesDbHelper.COLUMN_UPDATED_AT, System.currentTimeMillis())
        }
        val db = dbHelper.writableDatabase
        return db.update(
            NotesDbHelper.TABLE_NOTES,
            values,
            "${NotesDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // PUBLIC_INTERFACE
    fun deleteNote(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            NotesDbHelper.TABLE_NOTES,
            "${NotesDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun readNotesFromCursor(c: Cursor): List<Note> {
        val notes = mutableListOf<Note>()
        if (c.moveToFirst()) {
            do {
                notes.add(noteFromCursor(c))
            } while (c.moveToNext())
        }
        return notes
    }

    private fun noteFromCursor(c: Cursor): Note {
        val id = c.getLong(c.getColumnIndexOrThrow(NotesDbHelper.COLUMN_ID))
        val title = c.getString(c.getColumnIndexOrThrow(NotesDbHelper.COLUMN_TITLE))
        val content = c.getString(c.getColumnIndexOrThrow(NotesDbHelper.COLUMN_CONTENT))
        val updatedAt = c.getLong(c.getColumnIndexOrThrow(NotesDbHelper.COLUMN_UPDATED_AT))
        return Note(id = id, title = title, content = content, updatedAt = updatedAt)
    }
}
