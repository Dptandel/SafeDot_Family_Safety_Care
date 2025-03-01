package com.app.safedot_familysafetycare.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.safedot_familysafetycare.daos.ContactDao
import com.app.safedot_familysafetycare.models.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class SafeDotDB : RoomDatabase() {

    abstract fun contactDao(): ContactDao


    companion object {
        @Volatile
        private var INSTANCE: SafeDotDB? = null

        fun getDatabase(context: Context): SafeDotDB {
            INSTANCE?.let {
                return it
            }

            return synchronized(SafeDotDB::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SafeDotDB::class.java,
                    "safe_dot_db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}