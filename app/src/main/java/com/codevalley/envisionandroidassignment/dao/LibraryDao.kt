package com.codevalley.envisionandroidassignment.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codevalley.envisionandroidassignment.model.library.Library
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query("SELECT * FROM library ORDER BY date DESC")
    fun getLibrary(): Flow<List<Library>>

    @Query("DELETE FROM library")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParagraph(lLibrary: Library)

}