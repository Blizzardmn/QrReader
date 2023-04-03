package com.qr.myqr

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

inline fun <reified A : Activity> Activity.toActivity(
    bundle: Bundle? = null,
    requestCode: Int? = null
) {
    val intent = Intent(this, A::class.java)
    bundle?.let { intent.putExtras(it) }
    requestCode?.let {
        startActivityForResult(intent, requestCode)
    } ?: run {
        startActivity(intent)
    }
}

fun Context.shareTextToOtherApp(msg: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_TEXT,
        msg
    )
    startActivity(Intent.createChooser(intent, "share"))
}

fun Context.copyText(content: String) {
    val clipboard = getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(null, content)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, getString(R.string.copy_success),Toast.LENGTH_SHORT).show()
}
