package com.qr.myqr.scan

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.qr.myqr.R
import com.qr.myqr.basic.BasePage
import com.qr.myqr.databinding.ActivityScanningBinding
import com.qr.myqr.db.RoomImpl
import com.qr.myqr.db.ScanEntity
import com.qr.myqr.main.Type
import com.qr.myqr.toActivity

class ScanningActivity : BasePage() {
    private var isOpenLight = false
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var barcodeScanner: BarcodeScanner
    private val openGallery =
        registerForActivityResult(object : ActivityResultContract<Any?, Intent?>() {
            override fun createIntent(context: Context, input: Any?): Intent {
                return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                return if (resultCode == RESULT_OK) {
                    intent
                } else {
                    null
                }
            }
        }) {
            if (it != null) {
                val imageUri = it.data
                // 从 URI 获取所选图像的 Bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                syncGalleyBitmap(bitmap)
            }
        }
    override val binding by lazy { ActivityScanningBinding.inflate(layoutInflater) }

    override fun initView() {
        binding.run {
            ivBack.setOnClickListener { onBackPressed() }
            ivGallery.setOnClickListener {
                XXPermissions.with(this@ScanningActivity).permission(Permission.READ_MEDIA_IMAGES)
                    .request { _, allGranted ->
                        if (allGranted) {
                            openGallery.launch(null)
                        }
                    }
            }
            ivLight.setOnClickListener {
                if (isOpenLight) {
                    cameraController.imageCaptureFlashMode = FLASH_MODE_OFF
                    cameraController.enableTorch(false)
                } else {
                    cameraController.imageCaptureFlashMode = FLASH_MODE_ON
                    cameraController.enableTorch(true)
                }
            }
        }
        startCamera()
    }


    private fun startCamera() {
        cameraController = LifecycleCameraController(baseContext)
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        barcodeScanner = BarcodeScanning.getClient(options)
        cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(this),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(this)
            ) { result: MlKitAnalyzer.Result? ->
                val barcodeResults = result?.getValue(barcodeScanner)
                if ((barcodeResults == null) || (barcodeResults.size == 0) || (barcodeResults.first() == null)) {
                    return@MlKitAnalyzer
                }
                analyzerBorder(barcodeResults)
            })
        cameraController.bindToLifecycle(this)
        binding.previewView.controller = cameraController
        cameraController.torchState.observe(this@ScanningActivity) {
            Log.i("", "flash --->$it")
            isOpenLight = it == 1
            binding.ivLight.setImageResource(if (isOpenLight) R.drawable.ic_light_open else R.drawable.ic_light_close)
        }
    }

    private fun syncGalleyBitmap(bitmap: Bitmap) {
        val scanner = BarcodeScanning.getClient()
        val image: InputImage = InputImage.fromBitmap(bitmap, 0)
        scanner.process(image).addOnSuccessListener { barcodes ->
            analyzerBorder(barcodes, true)
        }.addOnFailureListener {
            Toast.makeText(this, "No data detected!", Toast.LENGTH_LONG).show()
        }
    }


    private fun analyzerBorder(barcodeList: List<Barcode>, isAnalyzerGallery: Boolean = false) {
        val stringBuilder = StringBuilder()
        var type = Barcode.TYPE_TEXT
        for (barcode in barcodeList) {
            when (barcode.valueType) {
                Barcode.TYPE_URL -> {
                    stringBuilder.append(barcode.url?.url)
                }
                else -> {
                    stringBuilder.append(barcode.rawValue.toString())
                }
            }
            type = barcode.valueType
        }
        if (isAnalyzerGallery && stringBuilder.isEmpty()) {
            Toast.makeText(this, "Detection failed", Toast.LENGTH_LONG).show()
            return
        }
        toResultPage(stringBuilder.toString(), type)
    }

    private fun toResultPage(string: String, barcodeType: Int) {
        cameraController.unbind()
        RoomImpl.scanDao.addScan(ScanEntity().apply {
            type = getUiType(barcodeType).ordinal
            content = string
        })
        toActivity<ScannerResultActivity>(bundle = Bundle().apply {
            putString("scanResult", string)
        })
        finish()
    }

    override fun onResume() {
        super.onResume()
        binding.run {
            ivScanningAnimator.post {
                ivScanningAnimator.animation = TranslateAnimation(
                    0f, 0f, 5f, binding.ivCodeRe.height.toFloat() - 10f
                ).apply {
                    duration = 1200
                    repeatCount = -1
                }
                ivScanningAnimator.animation.start()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        binding.ivScanningAnimator.animation?.cancel()
    }

    private fun getUiType(type: Int): Type {
        return when (type) {
            Barcode.TYPE_URL -> Type.Website
            Barcode.TYPE_EMAIL -> Type.Email
            Barcode.TYPE_PHONE -> Type.Tel
            Barcode.TYPE_SMS -> Type.SMS
            Barcode.TYPE_WIFI -> Type.WiFi
            else -> Type.Text
        }
    }
}