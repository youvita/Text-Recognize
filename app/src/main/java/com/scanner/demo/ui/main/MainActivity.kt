package com.scanner.demo.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.scanner.demo.R
import com.scanner.demo.app.Constants
import com.scanner.demo.app.Type
import com.scanner.demo.base.BaseActivity
import com.scanner.demo.biometric.BiometricPromptUtils
import com.scanner.demo.databinding.ActivityMainBinding
import com.scanner.demo.ui.info.AccountActivity
import com.scanner.demo.ui.info.model.AccountInfo

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        // Request camera permissions
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.camera.observe(this) {
            val biometricInfo = BiometricPromptUtils.createPromptInfo(this)
            BiometricPromptUtils.createBiometricPrompt(this) {
                openCamera()
            }.authenticate(biometricInfo)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                openCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * start default camera
     * */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityResult.launch(intent)
    }

    /**
     * activity for result
     * */
    private var startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            getTextImage(bitmap)
        }
    }

    /**
     * text recognize image
     * */
    private fun getTextImage(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
            .setLanguageHints(listOf("en", "km"))
            .build()
        val detector = FirebaseVision.getInstance()
            .getCloudDocumentTextRecognizer(options)

        detector.processImage(image)
            .addOnSuccessListener {
                // Task completed successfully
                // ...
                for (block in it.blocks) {
                    val blockText = block.text
                    Log.d(">>>>", blockText)
                }
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
            }

        // just set static depending on detect process image not working.
        // base no Google Cloud billing account not add yet.
        val accountInfo = AccountInfo()
        accountInfo.id_type = Type.KHMER_ID_TYPE
        accountInfo.id_number = "020721769"
        accountInfo.expiry_date = "04-09-2026"

        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent.putExtra(Constants.ACCOUNT_INFO_KEY, accountInfo))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}