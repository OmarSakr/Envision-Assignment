package com.codevalley.envisionandroidassignment.utils

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.codevalley.envisionandroidassignment.model.libraryRoomDatabase.LibraryRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class AppController : MultiDexApplication() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { LibraryRoomDatabase.getDatabase(this, applicationScope) }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}