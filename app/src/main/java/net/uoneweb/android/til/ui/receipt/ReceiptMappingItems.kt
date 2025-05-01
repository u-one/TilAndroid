package net.uoneweb.android.til.ui.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.receipt.data.Certainty
import net.uoneweb.android.til.ui.receipt.data.ComparisonItem
import net.uoneweb.android.til.ui.receipt.data.Difference
import net.uoneweb.android.til.ui.receipt.data.ExistingStoreTagItem
import net.uoneweb.android.til.ui.receipt.data.ExtractedTagItem

@Composable
fun ExtractedTagItem(item: ExtractedTagItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "${item.key}: ${item.value}",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            CertaintyLabel(item.basedOn)
        }
        Text(item.reason)
        Text(item.comment)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun PreviewExtractedTagItem() {
    Column {
        ExtractedTagItem(ExtractedTagItem("name", "hoge", Certainty.Fact, "reason", "comment"))
        ExtractedTagItem(ExtractedTagItem("name", "hoge", Certainty.Inference, "reason", "comment"))
    }
}

@Composable
fun ExistingStoreTagItem(item: ExistingStoreTagItem) {
    Row {
        Text(
            item.key,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleMedium,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(item.value, modifier = Modifier.padding(bottom = 8.dp))
            Text(item.comment)
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun PreviewExistingStoreTagItem() {
    ExistingStoreTagItem(ExistingStoreTagItem("name", "hoge", "fuga"))
}

@Composable
fun ComparingItem(item: ComparisonItem) {
    Row {
        Column {
            DifferenceLabel(difference = item.difference)
            Text(text = item.key, style = MaterialTheme.typography.titleMedium)
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text("${item.existingValue} -> ${item.newValue}", modifier = Modifier.padding(bottom = 8.dp))
            Text(item.comment)
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun PreviewComparingItem() {
    Column {
        ComparingItem(ComparisonItem("name", "hoge", "fuga", Difference.New, "comment"))
        HorizontalDivider()
        ComparingItem(ComparisonItem("name", "hoge", "fuga", Difference.Different, "comment"))
        HorizontalDivider()
        ComparingItem(ComparisonItem("name", "hoge", "fuga", Difference.Same, "comment"))
    }
}


@Composable
fun CertaintyLabel(certainty: Certainty, modifier: Modifier = Modifier) {
    val color = when (certainty) {
        Certainty.Unknown -> Color.Gray
        Certainty.Fact -> Color.Green
        Certainty.Inference -> Color.Yellow
    }
    val text = when (certainty) {
        Certainty.Unknown -> "Unknown"
        Certainty.Fact -> "事実ベース"
        Certainty.Inference -> "予測ベース"
    }
    Box(
        modifier = modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(text)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCertaintyLabel() {
    CertaintyLabel(Certainty.Fact)
}

@Composable
fun DifferenceLabel(difference: Difference) {
    val color = when (difference) {
        Difference.Unknown -> Color.Gray
        Difference.Same -> Color.Gray
        Difference.Different -> Color.Yellow
        Difference.New -> Color.Red

    }
    val text = when (difference) {
        Difference.Unknown -> "Unknown"
        Difference.Same -> "差異なし"
        Difference.Different -> "更新"
        Difference.New -> "新規"
    }
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDifferenceLabel() {
    DifferenceLabel(Difference.Different)
}
