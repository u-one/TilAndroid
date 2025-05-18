package net.uoneweb.android.gis.ui.map

data class Style(val url: String, val name: String = "", val org: String = "", val description: String = "") {
    companion object {

        val JpMieruneStreets = Style("https://api.maptiler.com/maps/jp-mierune-streets/style.json?key=397cVdOrWVJxbRXCtkW1", "jp-mierune-streets", "MapTiler")
        val Rekichizu = Style("https://mierune.github.io/rekichizu-style/styles/street/style.json", "れきちず")
        val OsmJpBright = Style("https://tile.openstreetmap.jp/styles/osm-bright/style.json", "Bright", "OSM JP")
        val OsmJpBrightJa = Style("https://tile.openstreetmap.jp/styles/osm-bright-ja/style.json", "Bright ja", "OSM JP")
        val OsmJpMaptilerBasicEn = Style("https://tile.openstreetmap.jp/styles/maptiler-basic-en/style.json", "MapTiler Basic En", "OSM JP")

        val Default = OsmJpBrightJa

        val MapLibre = Style("https://api.maplibre.org/styles/v1/maplibre/streets-v11")
        val MapLibreDark = Style("https://api.maplibre.org/styles/v1/maplibre/dark-v10")
        val MapLibreLight = Style("https://api.maplibre.org/styles/v1/maplibre/light-v10")
        val MapLibreSatellite = Style("https://api.maplibre.org/styles/v1/maplibre/satellite-v9")

        val Empty = Style("")
    }
}

object Styles {
    val styles = listOf(
        Style.JpMieruneStreets,
        Style.Rekichizu,
        Style.OsmJpBright,
        Style.OsmJpBrightJa,
        Style.OsmJpMaptilerBasicEn,
        Style.Default,
        Style.MapLibre,
        Style.MapLibreDark,
        Style.MapLibreLight,
        Style.MapLibreSatellite,
    )

    fun fromUri(uri: String): Style {
        return styles.firstOrNull { it.url == uri } ?: Style(uri)
    }

}

