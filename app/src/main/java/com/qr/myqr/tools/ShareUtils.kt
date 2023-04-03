package com.qr.myqr.tools

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.qr.myqr.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Date：2023/4/3
 * Describe:
 */
object ShareUtils {

    //need permission
    fun shareBitmapToOtherApp(context: Context, bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            context.applicationContext.contentResolver,
            bitmap,
            context.resources.getString(R.string.app_name),
            null
        )
        val uri = Uri.parse(path)
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(
                Intent.createChooser(
                    this, context.resources.getString(R.string.app_name)
                )
            )
        }
    }

    fun saveBitmapToMedia(
        context: Context,
        bitmap: Bitmap,
        saveSuccess: () -> Unit = {},
        saveFailed: () -> Unit = {}
    ) {
        val filename = "qrImage${System.currentTimeMillis()}.jpg"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(path, filename)
        Log.i("saveBitmapToMedia", "path--> $path")
        runCatching {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
//            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//            mediaScanIntent.data = Uri.fromFile(file)
//            context.sendBroadcast(mediaScanIntent)
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf(file.absolutePath), null
            ) { path, uri ->
                // 扫描完成后的操作
                saveSuccess.invoke()
            }
        }.onFailure {
            saveBitmap(context, bitmap, saveSuccess,saveFailed)
            Log.e("saveBitmapToMedia", "saveBitmapToMedia: ${it.message}")
        }
    }

    //部分手机拿不到 Environment.getExternalStoragePublicDirectory
    private fun saveBitmap(context: Context, bitmap: Bitmap, saveSuccess: () -> Unit = {},   saveFailed: () -> Unit = {}) {
        runCatching {
            val path: String = MediaStore.Images.Media.insertImage(
                context.applicationContext.contentResolver,
                bitmap,
                context.resources.getString(R.string.app_name),
                null
            )
            Log.i("saveBitmap", "saveBitmapToMedia: $path")
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf(path), null
            ) { path1, uri ->
                // 扫描完成后的操作
                saveSuccess.invoke()
            }
        }.onFailure {
            saveFailed.invoke()
            Log.e("saveBitmap", "saveBitmapToMedia: ${it.stackTrace}")
        }

    }
}