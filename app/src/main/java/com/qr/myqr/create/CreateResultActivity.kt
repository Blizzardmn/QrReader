package com.qr.myqr.create

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.qr.myqr.R
import com.qr.myqr.basic.BasePage
import com.qr.myqr.copyText
import com.qr.myqr.databinding.ActivityCreateResultBinding
import com.qr.myqr.revenue.AdPos
import com.qr.myqr.revenue.AdsListener
import com.qr.myqr.revenue.AdsLoader
import com.qr.myqr.revenue.ad.BaseAd
import com.qr.myqr.revenue.ad.TopNative
import com.qr.myqr.tools.ShareUtils
import com.qr.myqr.tools.checkReadMediaPermission

/**
 * Date：2023/4/3
 * Describe:
 */
class CreateResultActivity : BasePage() {
    private var bitmap: Bitmap? = null
    private var result = ""
    override val binding by lazy { ActivityCreateResultBinding.inflate(layoutInflater) }

    override fun initView() {
        result = intent.getStringExtra("content") ?: ""
        bitmap = qrCode(result, dpToPx(95), dpToPx(95))
        binding.ivQrCode.setImageBitmap(bitmap)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivCopy.setOnClickListener {
            copyText(result)
        }
        binding.tvShare.setOnClickListener {
            bitmap?.let {
                checkReadMediaPermission {
                    ShareUtils.shareBitmapToOtherApp(this, it)
                }
            }
        }
        binding.tvAction.setOnClickListener {
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

        showNavAd()
    }

    private fun showNavAd() {
        AdsLoader.loadAd(this, AdPos.navResult, object : AdsListener() {
            override fun onLoadedAd(ad: BaseAd) {
                if (ad !is TopNative) return
                ad.showAd(this@CreateResultActivity, binding.nativeAdView, binding.nativeSelfRender.root)

                AdsLoader.preloadAd(this@CreateResultActivity, AdPos.navResult)
            }
        })
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