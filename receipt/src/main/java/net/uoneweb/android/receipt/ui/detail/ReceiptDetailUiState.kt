package net.uoneweb.android.receipt.ui.detail

import android.net.Uri
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.receipt.data.ReceiptMetaData

data class ReceiptDetailUiState(
    val selectedImageUri: Uri? = null,
    val uploadedImageUri: Uri? = null,
    val receipt: ReceiptMetaData = ReceiptMetaData.Empty,
    val imageLocation: Location = Location.Empty,
    val location: Location? = Location.Default,
    val loading: Boolean = false,
    val saved: Boolean = false,
    val correctionText: String = "",
)
