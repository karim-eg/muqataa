/*
 * Copyright (c) 2023-2024. Kotect Company, https://kotect.com
 * Main Programmer: Karim Abdallah
 * contact: support@kotect.com
 */

package co.encept.muqataa.ui

import android.Manifest
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.encept.muqataa.HandleBarCode
import co.encept.muqataa.R
import co.encept.muqataa.databinding.FragmentScannerBinding
import co.encept.muqataa.ui.models.ValidateBarcodeModel
import com.google.android.gms.ads.AdRequest
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.json.JSONObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScannerFragment : Fragment() {
    private lateinit var bind: FragmentScannerBinding

    private val validateModel: ValidateBarcodeModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null

    private var camera: Camera? = null

    private var isFlashOn = false
    private var isCoolDown = false

    private lateinit var vb: Vibrator


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentScannerBinding.inflate(layoutInflater)

        validateModel.initData(requireContext())

        (bind.root).keepScreenOn = false

        bind.ad.loadAd(AdRequest.Builder().build())

        vb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = activity?.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            activity?.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }


        bind.apply {
            imgFlash.setOnClickListener {
                isFlashOn = if (isFlashOn) {
                    flashManager(false)
                    imgFlashIcon.setImageResource(R.drawable.ic_flash_on)
                    false
                } else {
                    flashManager(true)
                    imgFlashIcon.setImageResource(R.drawable.ic_flash_off)
                    true
                }
            }
        }

        return bind.root
    }


    override fun onPause() {
        super.onPause()
        isFlashOn = false
        view?.keepScreenOn = false
        flashManager(status = false, vb = false)
        bind.imgFlashIcon.setImageResource(R.drawable.ic_flash_on)
    }


    override fun onDestroy() {
        super.onDestroy()
        view?.keepScreenOn = false
        cameraExecutor.shutdown()
    }


    private fun startCamera() {
        val handleBarCode = HandleBarCode()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())


        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODABAR,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E)
            .build()


        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(bind.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()


            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val imageFrame = imageProxy.toBitmap()
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                val scanner: BarcodeScanner = BarcodeScanning.getClient(options)
                val image = InputImage.fromBitmap(imageFrame, rotationDegrees)


                scanner.process(image).addOnSuccessListener { barcodes ->
                    if (isVisible || isAdded) {
                        for (barcode in barcodes) {
                            val barcodeValue = barcode.displayValue
                            bind.apply {
                                activity?.runOnUiThread {
                                    if (!txtResult.isVisible || !cardResult.isVisible) {
                                        txtResult.visibility = View.VISIBLE
                                        cardResult.visibility = View.VISIBLE
                                    }

                                    val check = validateModel.checkMuqataa(barcodeValue ?: "error")

                                    if (check) {
                                        if (!isCoolDown) {
                                            vibrate(700)
                                            isCoolDown = true
                                        }
                                        txtResult.text = getString(R.string.product_banned)
                                        txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

                                    } else {
                                        if (!isCoolDown) {
                                            vibrate(70)
                                            isCoolDown = true
                                        }
                                        txtResult.text = getString(R.string.product_okay)
                                        txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                                    }

                                    txtBarcode.text = barcodeValue

                                    val country = handleBarCode.getCountry(requireContext(), barcodeValue ?: "error")
                                    txtCountry.text = country
                                    if (country == getString(R.string.pigs)) {
                                        txtCountry.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                        vibrate(700)

                                    } else {
                                        txtCountry.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                    }
                                }
                            }
                        }
                    }

                }.addOnFailureListener {
                    activity?.runOnUiThread {
                        bind.apply {
                            txtResult.text = getString(R.string.product_unknown)
                            txtCountry.text = getString(R.string.product_country)
                            txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }

                    }
                }

                imageProxy.close()
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

        }, ContextCompat.getMainExecutor(requireContext()))


        // vibrate every 3 sec to prevent multiple vibration in a short time
        val runnable = object : Runnable {
            override fun run() {
                isCoolDown = false
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }


    private fun flashManager(status: Boolean, vb: Boolean = true) {
        if (vb) vibrate(100)
        val cameraControl = camera?.cameraControl
        cameraControl?.enableTorch(status)
    }


    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vb.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vb.vibrate(duration)
        }
    }


    private fun requestPermissions() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(requireActivity().baseContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in Manifest.permission.CAMERA && !it.value) permissionGranted = false
        }

        if (!permissionGranted) {
            Toast.makeText(activity?.baseContext, "Permission request denied", Toast.LENGTH_SHORT).show()
        } else {
            startCamera()
        }
    }
}