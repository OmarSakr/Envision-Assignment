package com.codevalley.envisionandroidassignment.model.library

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class Library(
    val paragraph: String,
    @PrimaryKey val date: String
)
