package net.uoneweb.android.receipt.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import net.uoneweb.android.gis.ui.location.Location

class ReceiptImage(
    private val contentResolver: ContentResolver,
    private val uri: Uri,
) {
    fun bitmap(): Bitmap {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun location(): Location {
        val inputStream = contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)
        if (exif.latLong == null || exif.latLong?.size != 2) {
            return Location.Empty
        }
        val lat = exif.latLong!!.get(0)
        val lon = exif.latLong!!.get(1)
        return Location(lat, lon)
    }
}