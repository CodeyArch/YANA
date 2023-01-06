package me.goobydev.composenotes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goobydev.composenotes.feature_note.data.data_source.NoteDatabase
import me.goobydev.composenotes.feature_note.data.repository.NoteRepositoryImpl
import me.goobydev.composenotes.feature_note.domain.repository.NoteRepository
import me.goobydev.composenotes.feature_note.domain.use_case.*
import me.goobydev.composenotes.feature_note.domain.use_case.NoteUseCases
import javax.inject.Singleton

/* Provides the app its database, repository and use cases. It is a singleton so that there are
not multiple versions of these items */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}