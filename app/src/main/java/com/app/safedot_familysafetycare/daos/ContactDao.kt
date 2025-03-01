package com.app.safedot_familysafetycare.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.safedot_familysafetycare.models.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactList: List<Contact>)

    @Query("SELECT * FROM contact")
    fun getAllContacts(): LiveData<List<Contact>>
}