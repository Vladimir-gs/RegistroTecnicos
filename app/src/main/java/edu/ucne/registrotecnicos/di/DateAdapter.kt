package edu.ucne.registrotecnicos.di

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.let

class DateAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    @ToJson
    fun toJson(value: Date?): String? {
        return value?.let { dateFormat.format(it) }
    }

    @FromJson
    fun fromJson(value: String?): Date? {
        return value?.let { dateFormat.parse(it) }
    }
}