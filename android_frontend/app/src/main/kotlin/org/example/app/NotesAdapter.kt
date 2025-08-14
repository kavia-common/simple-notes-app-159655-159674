package org.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.data.Note
import java.text.DateFormat
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * RecyclerView adapter for showing a list of notes.
 * Use submitList() to update the data shown in the list.
 */
class NotesAdapter(private val onClick: (Note) -> Unit) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private val items = mutableListOf<Note>()

    // PUBLIC_INTERFACE
    fun submitList(list: List<Note>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, onClick)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class NoteViewHolder(itemView: View, private val onClick: (Note) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.noteTitle)
        private val content: TextView = itemView.findViewById(R.id.noteContent)
        private val date: TextView = itemView.findViewById(R.id.noteDate)
        private var currentNote: Note? = null

        init {
            itemView.setOnClickListener {
                currentNote?.let { onClick(it) }
            }
        }

        fun bind(note: Note) {
            currentNote = note
            title.text = note.title
            content.text = note.content.take(120)
            val dt = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            date.text = dt.format(Date(note.updatedAt))
        }
    }
}
