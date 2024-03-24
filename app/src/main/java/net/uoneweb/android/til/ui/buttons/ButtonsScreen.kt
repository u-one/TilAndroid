package net.uoneweb.android.til.ui.buttons

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.uoneweb.android.til.ui.buttons.draggablegrid.DraggableGrid

@Composable
fun ButtonsScreen() {
    DraggableGrid()
}

@Preview(
    showBackground = true,
)
@Composable
private fun ButtonsScreenPreview() {
    MaterialTheme {
        ButtonsScreen()
    }
}