package com.dicoding.mechanicalkeyboards

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Keyboard(
    val name: String,
    val description: String,
    val photo: String,
    val specification: String
) : Parcelable
