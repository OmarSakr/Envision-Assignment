package com.codevalley.envisionandroidassignment.model.libraryRoomDatabase

import android.content.Context
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.codevalley.envisionandroidassignment.dao.LibraryDao
import com.codevalley.envisionandroidassignment.model.library.Library
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4ClassRunner::class)
class LibraryRoomDatabaseTest {
    private lateinit var db: LibraryRoomDatabase
    private lateinit var dao: LibraryDao
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, LibraryRoomDatabase::class.java).build()
        dao = db.libraryDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun addAndReadCoffee() = runBlocking {
        val library = Library("today is a fine day", "Thu, Nov 11 AT 20:59 PM")
        dao.insertParagraph(library)
        val paragraph = dao.getLibrary()
        TestCase.assertNotNull(paragraph)
    }
}