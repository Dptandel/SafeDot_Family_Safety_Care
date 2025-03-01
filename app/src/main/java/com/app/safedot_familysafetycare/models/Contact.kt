package com.app.safedot_familysafetycare.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    val name: String,
    @PrimaryKey
    val number: String
)