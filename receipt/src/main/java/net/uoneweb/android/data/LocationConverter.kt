package net.uoneweb.android.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import net.uoneweb.android.gis.ui.location.Location

object LocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun toJson(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun fromJson(json: String): Location {
        return gson.fromJson(json, Location::class.java)
    }
}