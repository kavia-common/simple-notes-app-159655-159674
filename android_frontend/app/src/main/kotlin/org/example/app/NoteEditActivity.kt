package org.example.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import org.example.app.data.NotesRepository

/**
 * PUBLIC_INTERFACE
 * NoteEditActivity allows creating a new note or editing an existing one.
 * - If EXTRA_NOTE_ID is provided (> 0), it loads the note for editing.
 * - Otherwise, it allows creating a new note.
 */
class NoteEditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private lateinit var repository: NotesRepository
    private var noteId: Long = -1L

    private lateinit var titleInput: TextInputEditText
    private lateinit var contentInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        repository = NotesRepository(this)

        titleInput = findViewById(R.id.inputTitle)
        contentInput = findViewById(R.id.inputContent)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
        if (noteId > 0) {
            supportActionBar?.title = getString(R.string.edit_note)
            val note = repository.getNoteById(noteId)
            if (note != null) {
                titleInput.setText(note.title)
                contentInput.setText(note.content)
            }
        } else {
            supportActionBar?.title = getString(R.string.new_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_edit, menu)
        val deleteItem = menu.findItem(R.id.action_delete)
        deleteItem.isVisible = noteId > 0
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                saveNote()
                true
            }
            R.id.action_delete -> {
                deleteNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // PUBLIC_INTERFACE
    private fun saveNote() {
        val title = titleInput.text?.toString()?.trim() ?: ""
        val content = contentInput.text?.toString()?.trim() ?: ""
        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_title_required), Toast.LENGTH_SHORT).show()
            return
        }
        if (noteId > 0) {
            repository.updateNote(noteId, title, content)
        } else {
            repository.insertNote(title, content)
        }
        finish()
    }

    // PUBLIC_INTERFACE
    private fun deleteNote() {
        if (noteId > 0) {
            repository.deleteNote(noteId)
        }
        finish()
    }
}
