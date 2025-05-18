package net.uoneweb.android.gis.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.maplibre.geojson.Feature

@Composable
fun FeatureList(mapViewState: MapViewState, modifier: Modifier = Modifier) {
    val list = mapViewState.features.filter { it.geometry()?.type() != "LineString" || it.geometry()?.type() != "MultiLineString" }
    val points = mapViewState.features.filter { it.geometry()?.type() == "Point" }
    val polygons = mapViewState.features.filter { it.geometry()?.type() == "Polygon" }

    LazyColumn(modifier = modifier) {
        item {
            Text("total:${mapViewState.features.size} points:${points.size} polygons:${polygons.size}", modifier = Modifier.padding(8.dp))
        }
        items(list) { feature ->
            FeatureItem(feature)
        }

    }
}

@Composable
fun FeatureItem(feature: Feature, onClick: (Feature) -> Unit = {}) {
    val id = feature.getStringProperty("id")
    val name = feature.getStringProperty("name")
    val class_name = feature.getStringProperty("class")
    val properties = feature.properties()
    Column {
        HorizontalDivider()
        Text(
            text = "$name id:$id class:$class_name $properties",
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeatureListPreview() {
    val state = MapViewState()
    val features = listOf(
        Feature.fromGeometry(null),
        Feature.fromGeometry(null),
        Feature.fromGeometry(null),
    )
    state.updateFeatures(features)
    FeatureList(state)
}