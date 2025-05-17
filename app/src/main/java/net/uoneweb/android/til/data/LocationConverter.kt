package net.uoneweb.android.til.data

import androidx.room.TypeConverter
import com.google.gson.Gson

object LocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun toJson(location: net.uoneweb.android.gis.ui.location.Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun fromJson(json: String): net.uoneweb.android.gis.ui.location.Location {
        return gson.fromJson(json, net.uoneweb.android.gis.ui.location.Location::class.java)
    }
}