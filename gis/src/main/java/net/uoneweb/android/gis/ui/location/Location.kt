package net.uoneweb.android.gis.ui.location

import java.util.Locale

@JvmInline
value class Coordinate(val value: Double) {
    override fun toString(): String {
        return String.format(Locale.JAPAN, "%.6f", value)
    }
}

class Location(
    lat: Double,
    lon: Double,
) {
    val latitude: Coordinate = Coordinate(lat)
    val longitude: Coordinate = Coordinate(lon)

    companion object {
        val Empty = Location(0.0, 0.0)
        val Default = Location(35.68038, 139.76751) // Tokyo Station
    }
}
