package com.q8munasabat.imagepicker.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.q8munasabat.R
import java.io.File

/**
 * Get Gallery/Camera Intent
 */
object IntentUtils {

    /**
     * @return Intent Gallery Intent
     */
    fun getGalleryIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var intent = getGalleryDocumentIntent()
            if (intent.resolveActivity(context.packageManager) == null) {
                // No Activity found that can handle this intent.
                intent = getGalleryPickIntent()
            }
            intent
        } else {
            getGalleryPickIntent()
        }
    }

    /**
     * @return Intent Gallery Document Intent
     */
    private fun getGalleryDocumentIntent(): Intent {
        // Show Document Intent
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Apply filter to show image only in intent
        intent.type = "image/*"

        return intent
    }

    /**
     * @return Intent Gallery Pick Intent
     */
    private fun getGalleryPickIntent(): Intent {
        // Show Gallery Intent, Will open google photos
        val intent = Intent(Intent.ACTION_PICK)

        // Apply filter to show image only in intent
        intent.type = "image/*"

        return intent
    }

    /**
     * @return Intent Camera Intent
     */
    fun getCameraIntent(context: Context, file: File): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // authority = com.q8munasabat.imagepicker.provider
            val authority = context.getString(R.string.file_provider_authorities)
            val photoURI = FileProvider.getUriForFile(context, authority, file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        }

        return intent
    }

    fun isCameraHardwareAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}
