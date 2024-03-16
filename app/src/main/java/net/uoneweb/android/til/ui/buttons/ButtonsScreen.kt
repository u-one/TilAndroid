package net.uoneweb.android.til.ui.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonsScreen() {
    val list = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )

    LazyVerticalGrid(columns = GridCells.Adaptive(64.dp)) {
        items(list) { item ->
            ItemButton(item)
        }
    }
}

@Composable
private fun ItemButton(item: String) {
    Box(modifier = Modifier
        .size(64.dp)
        .padding(8.dp)) {
        Button(modifier = Modifier.size(56.dp), onClick = {}) {
            Text(item)
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun ItemButtonPreview() {
    MaterialTheme {
        ItemButton("Text")
    }
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