package com.qr.myqr.create

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.qr.myqr.R
import com.qr.myqr.basic.BasePage
import com.qr.myqr.copyText
import com.qr.myqr.databinding.ActivityCreateResultBinding
import com.qr.myqr.tools.ShareUtils
import com.qr.myqr.tools.checkReadMediaPermission

/**
 * Date：2023/4/3
 * Describe:
 */
class CreateResultActivity : BasePage() {
    private var bitmap: Bitmap? = null
    private var result = ""
    override val viewBinding by lazy { ActivityCreateResultBinding.inflate(layoutInflater) }

    override fun initView() {
        result = intent.getStringExtra("content") ?: ""
        bitmap = qrCode(result, dpToPx(95), dpToPx(95))
        viewBinding.ivQrCode.setImageBitmap(bitmap)
        viewBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
        viewBinding.ivCopy.setOnClickListener {
            copyText(result)
        }
        viewBinding.tvShare.setOnClickListener {
            bitmap?.let {
                checkReadMediaPermission {
                    ShareUtils.shareBitmapToOtherApp(this, it)
                }
            }
        }
        viewBinding.tvAction.setOnClickListener {
            bitmap?.let {
                checkReadMediaPermission {
                    ShareUtils.saveBitmapToMedia(this, it, saveFailed = {
                        runOnUiThread {
                            Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show()
                        }
                    }, saveSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }


    private fun qrCode(data: String, width: Int, height: Int): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun dpToPx(dp: Int): Int = (resources.displayMetrics.density * dp).toInt()

}