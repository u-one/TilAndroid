package net.uoneweb.android.til.ui.camera

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

typealias BarcodeListener = (barcode: Barcode) -> Unit

class BarcodeAnalyzer(private val listener: BarcodeListener) : ImageAnalysis.Analyzer {
    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            val scanner = BarcodeScanning.getClient()

            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        listener(barcode)
                        Log.i(
                            TAG,
                            "${barcode.format}, ${barcode.valueType}, ${barcode.displayValue}, ${barcode.boundingBox}"
                        )
                    }
                    Log.d(TAG, barcodes.map { it.toString() }.joinToString())
                }
                .addOnFailureListener {
                    //Log.e(TAG, "Failed to analyze ", it)
                }
        }
        imageProxy.close()
    }

    companion object {
        const val TAG = "BarcodeAnalyzer"
    }

}