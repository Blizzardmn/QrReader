package com.qr.myqr.tools

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.util.Log
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * Date：2023/4/3
 * Describe:
 */

fun Activity.checkReadMediaPermission(checkFinish: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        checkFinish.invoke()
    } else {
        Log.i("TAG", "checkReadMediaPermission: ${Build.VERSION.SDK_INT}  ")
        XXPermissions.with(this).permission(Permission.READ_MEDIA_IMAGES)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (allGranted) {
                        checkFinish.invoke()
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    super.onDenied(permissions, doNotAskAgain)
                    if (doNotAskAgain) {
                        startPermissionDialog(permissions)
                    }
                }
            })
    }
}

fun Activity.checkCameraPermission(checkFinish: () -> Unit) {
    XXPermissions.with(this).permission(Permission.CAMERA).request(object : OnPermissionCallback {
        override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
            if (allGranted) {
                checkFinish.invoke()
            }
        }

        override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
            super.onDenied(permissions, doNotAskAgain)
            if (doNotAskAgain) {
                startPermissionDialog(permissions)
            }
        }
    })
}

fun Activity.startPermissionDialog(permissions: MutableList<String>) {
    AlertDialog.Builder(this@startPermissionDialog)
        .setMessage("Permission grant failed, do you want to grant it again?")
        .setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(
                this@startPermissionDialog, permissions
            )
        }.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }.create().show()
}
