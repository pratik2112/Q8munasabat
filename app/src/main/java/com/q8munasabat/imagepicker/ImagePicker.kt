package com.q8munasabat.imagepicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.q8munasabat.imagepicker.ImagePickerActivity
import com.q8munasabat.imagepicker.constant.ImageProvider
import com.q8munasabat.imagepicker.listener.ResultListener
import com.q8munasabat.imagepicker.util.DialogHelper
import java.io.File

open class ImagePicker {

    companion object {
        // Default Request Code to Pick Image
        const val REQUEST_CODE = 2404
        const val RESULT_ERROR = 64

        internal const val EXTRA_IMAGE_PROVIDER = "extra.image_provider"
        internal const val EXTRA_IMAGE_MAX_SIZE = "extra.image_max_size"
        internal const val EXTRA_CROP = "extra.crop"
        internal const val EXTRA_CROP_X = "extra.crop_x"
        internal const val EXTRA_CROP_Y = "extra.crop_y"
        internal const val EXTRA_MAX_WIDTH = "extra.max_width"
        internal const val EXTRA_MAX_HEIGHT = "extra.max_height"

        internal const val EXTRA_ERROR = "extra.error"
        internal const val EXTRA_FILE_PATH = "extra.file_path"

        /**
         * Use this to use ImagePicker in Activity Class
         *
         * @param activity Activity Instance
         */
        fun with(activity: Activity): Builder {
            return Builder(activity)
        }

        /**
         * Use this to use ImagePicker in Fragment Class
         *
         * @param fragment Fragment Instance
         */
        fun with(fragment: Fragment): Builder {
            return Builder(fragment)
        }

        /**
         * Get error message from intent
         */
        fun getError(data: Intent?): String {
            val error = data?.getStringExtra(EXTRA_ERROR)
            if (error != null) {
                return error
            } else {
                return "Unknown Error!"
            }
        }

        /**
         * Get File Path from intent
         */
        fun getFilePath(data: Intent?): String? {
            return data?.getStringExtra(EXTRA_FILE_PATH)
        }

        /**
         * Get File from intent
         */
        fun getFile(data: Intent?): File? {
            val path = getFilePath(data)
            if (path != null) {
                return File(path)
            }
            return null
        }
    }

    class Builder(private val activity: Activity) {

        private var fragment: Fragment? = null

        // Image Provider
        private var imageProvider = ImageProvider.BOTH

        /*
         * Crop Parameters
         */
        private var cropX: Float = 0f
        private var cropY: Float = 0f
        private var crop: Boolean = false

        /*
         * Resize Parameters
         */
        private var maxWidth: Int = 0
        private var maxHeight: Int = 0

        /*
         * Max File Size
         */
        private var maxSize: Long = 0

        /**
         * Call this while picking image for fragment.
         */
        constructor(fragment: Fragment) : this(fragment.activity!!) {
            this.fragment = fragment
        }

        /**
         * Specify Image Provider (Camera, Gallery or Both)
         */
        fun provider(imageProvider: ImageProvider): Builder {
            this.imageProvider = imageProvider
            return this
        }

        /**
         * Only Capture image using Camera.
         */
        // @Deprecated("Please use provider(ImageProvider.CAMERA) instead")
        fun cameraOnly(): Builder {
            this.imageProvider = ImageProvider.CAMERA
            return this
        }

        /**
         * Only Pick image from gallery.
         */
        // @Deprecated("Please use provider(ImageProvider.GALLERY) instead")
        fun galleryOnly(): Builder {
            this.imageProvider = ImageProvider.GALLERY
            return this
        }

        /**
         * Set an aspect ratio for crop bounds.
         * User won't see the menu with other ratios options.
         *
         * @param x aspect ratio X
         * @param y aspect ratio Y
         */
        fun crop(x: Float, y: Float): Builder {
            cropX = x
            cropY = y
            return crop()
        }

        /**
         * Crop an image and let user set the aspect ratio.
         */
        fun crop(): Builder {
            this.crop = true
            return this
        }

        /**
         * Crop Square Image, Useful for Profile Image.
         *
         */
        fun cropSquare(): Builder {
            return crop(1f, 1f)
        }

        /**
         * Max Width and Height of final image
         */
        fun maxResultSize(width: Int, height: Int): Builder {
            this.maxWidth = width
            this.maxHeight = height
            return this
        }

        /**
         * Compress Image so that max image size can be below specified size
         *
         * @param maxSize Size in KB
         */
        fun compress(maxSize: Int): Builder {
            this.maxSize = maxSize * 1024L
            return this
        }

        /**
         * Start Image Picker Activity
         */
        fun start() {
            start(REQUEST_CODE)
        }

        /**
         * Start Image Picker Activity
         */
        fun start(reqCode: Int) {
            if (imageProvider == ImageProvider.BOTH) {
                // Pick Image Provider if not specified
                showImageProviderDialog(reqCode)
            } else {
                startActivity(reqCode)
            }
        }

        /**
         * Start Image Picker Activity
         */
        fun start(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
            if (imageProvider == ImageProvider.BOTH) {
                // Pick Image Provider if not specified
                showImageProviderDialog(completionHandler)
            } else {
                startActivity(completionHandler)
            }
        }

        /**
         * Pick Image Provider if not specified
         */
        private fun showImageProviderDialog(reqCode: Int) {
            DialogHelper.showChooseAppDialog(activity, object : ResultListener<ImageProvider> {
                override fun onResult(t: ImageProvider?) {
                    t?.let {
                        imageProvider = it
                        startActivity(reqCode)
                    }
                }
            })
        }

        /**
         * Pick Image Provider if not specified
         */
        private fun showImageProviderDialog(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
            DialogHelper.showChooseAppDialog(activity, object : ResultListener<ImageProvider> {
                override fun onResult(t: ImageProvider?) {
                    if (t != null) {
                        imageProvider = t
                        startActivity(completionHandler)
                    } else {
                        val intent = ImagePickerActivity.getCancelledIntent(activity)
                        completionHandler?.invoke(Activity.RESULT_CANCELED, intent)
                    }
                }
            })
        }

        /**
         * Get Bundle for ImagePickerActivity
         */
        private fun getBundle(): Bundle {
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_IMAGE_PROVIDER, imageProvider)

            bundle.putBoolean(EXTRA_CROP, crop)
            bundle.putFloat(EXTRA_CROP_X, cropX)
            bundle.putFloat(EXTRA_CROP_Y, cropY)

            bundle.putInt(EXTRA_MAX_WIDTH, maxWidth)
            bundle.putInt(EXTRA_MAX_HEIGHT, maxHeight)

            bundle.putLong(EXTRA_IMAGE_MAX_SIZE, maxSize)

            return bundle
        }

        /**
         * Start ImagePickerActivity with given Argument
         */
        private fun startActivity(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {

            try {
                val intent = Intent(activity, ImagePickerActivity::class.java)
                intent.putExtras(getBundle())
                if (fragment != null) {

                    fragment?.startForResult(intent) { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }?.onFailed { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }
                } else {
                    (activity as AppCompatActivity).startForResult(intent) { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }.onFailed { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }
                }
            } catch (e: Exception) {
                if (e is ClassNotFoundException) {
                    Toast.makeText(if (fragment != null) fragment!!.context else activity, "InlineActivityResult library not installed falling back to default method, please install " +
                            "it from https://github.com/florent37/InlineActivityResult if you want to get inline activity results.", Toast.LENGTH_LONG).show()
                    startActivity(REQUEST_CODE)
                }
            }
        }

        /**
         * Start ImagePickerActivity with given Argument
         */
        private fun startActivity(reqCode: Int) {
            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtras(getBundle())
            if (fragment != null) {
                fragment?.startActivityForResult(intent, reqCode)
            } else {
                activity.startActivityForResult(intent, reqCode)
            }
        }
    }
}
