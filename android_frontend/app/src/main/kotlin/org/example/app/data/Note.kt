package org.example.app.data

/**
 * PUBLIC_INTERFACE
 * Data model representing a Note entity.
 */
data class Note(
    /** Unique ID for the note (0 for unsaved notes). */
    val id: Long = 0,
    /** Title of the note. */
    val title: String,
    /** Full text content of the note. */
    val content: String,
    /** Last updated timestamp in milliseconds since epoch. */
    val updatedAt: Long
)
