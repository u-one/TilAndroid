package net.uoneweb.android.gis.ui.location

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val Empty = Location(0.0, 0.0)
        val Default = Location(35.68038, 139.76751) // Tokyo Station
    }
}
