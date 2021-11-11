package com.codevalley.envisionandroidassignment.view.fragments.capture

import android.Manifest
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codevalley.envisionandroidassignment.R
import com.codevalley.envisionandroidassignment.databinding.FragmentCaptureBinding
import java.io.File
import android.os.Build
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import java.util.concurrent.Executors
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.codevalley.envisionandroidassignment.utils.Status
import com.codevalley.envisionandroidassignment.model.library.Library
import com.codevalley.envisionandroidassignment.network.ApiHelper
import com.codevalley.envisionandroidassignment.utils.*
import com.codevalley.envisionandroidassignment.utils.Constants.FILENAME_FORMAT
import com.codevalley.envisionandroidassignment.utils.Constants.REQUEST_CODE_CAMERA_PERMISSION
import com.codevalley.envisionandroidassignment.viewModel.captureViewModel.CaptureViewModel
import com.codevalley.envisionandroidassignment.viewModel.captureViewModel.CaptureViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService


typealias lumaListener = (luma: Double) -> Unit

//CaptureFragment allows user to take image and get the OCR to save it to library
class CaptureFragment : BaseFragment<FragmentCaptureBinding>(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var navController: NavController
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var multipartImage: MultipartBody.Part
    private lateinit var captureViewModel: CaptureViewModel
    private var readDocument = ""

    override fun getViewBinding() = FragmentCaptureBinding.inflate(layoutInflater)

    override fun setUpViews() {
        initUi()
        initEventDriven()
    }


    private fun initUi() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment)
        requestPermission()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        captureViewModel = ViewModelProvider(
            this,
            CaptureViewModelFactory(
                ApiHelper(RetrofitClient.apiInterface),
                (requireActivity().application as AppController).database.libraryDao()
            )
        ).get(CaptureViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getReadDocument() {
        lifecycleScope.launchWhenStarted {
            captureViewModel.mutableStateFlowReadDocument.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.relativeLoading.visibility = VISIBLE
                        binding.tvCapture.visibility = GONE
                    }
                    Status.SUCCESS -> {
                        binding.relativeLoading.visibility = GONE
                        binding.tvCapture.visibility = VISIBLE
                        if (it.data?.body()?.response?.paragraphs?.isNotEmpty() == true) {
                            binding.relativeCamera.visibility = GONE
                            binding.relativeResults.visibility = VISIBLE

                            for (i in it.data.body()?.response?.paragraphs!!) {
                                readDocument += i.paragraph
                            }
                            binding.tvResults.text = readDocument
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                it.data?.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        binding.tvCapture.visibility = VISIBLE
                        Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun initEventDriven() {
        // Set up the listener for take photo
        binding.tvCapture.setOnClickListener { takePhoto() }
        // Set up the listener for saving to library
        binding.tvSave.setOnClickListener {
            insertParagraph()
        }
        binding.relativeLibrary.setOnClickListener {
            navController.navigate(R.id.action_captureFragment_to_libraryFragment)
        }
    }

    private fun insertParagraph() {
        val library = Library(
            binding.tvResults.text.toString(), SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis())
        )
        lifecycleScope.launchWhenStarted {
            captureViewModel.addParagraph(library)
        }
        //showing a SnackBar after the saving process is dong
        Snackbar.make(binding.tvSave, R.string.textSavedToLibrary, Snackbar.LENGTH_LONG)
            .setAction(R.string.goToLibray) {
                navController.navigate(R.id.action_captureFragment_to_libraryFragment)
            }
            .show()
    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("exception",exc.toString())
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    //convert file to Multipart ad upload it server
                    //checking for internet connection
                    if (NetworkHelper(requireActivity()).isNetworkConnected()){
                        getMultipartFromFileAndSendIt(photoFile)
                    }else{
                        Toast.makeText(requireActivity(), requireActivity().getString(R.string.pleaseMakeSureToConnectToInternet), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer {
                    })
                }
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
            }

        }, ContextCompat.getMainExecutor(requireActivity()))
    }


    private fun getOutputDirectory(): File {
//        externalCacheDir?.absolutePath
        val mediaDir = requireActivity().externalCacheDir?.absolutePath.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }


    private fun requestPermission() {
        if (CameraUtility.hasCameraPermissions(requireActivity())) {
            startCamera()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the camera permission to use this app",
                REQUEST_CODE_CAMERA_PERMISSION,
                Manifest.permission.CAMERA
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the camera permission to use this app",
                REQUEST_CODE_CAMERA_PERMISSION,
                Manifest.permission.CAMERA

            )
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private class LuminosityAnalyzer(private val listener: lumaListener) : ImageAnalysis.Analyzer {
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }


    private fun getMultipartFromFileAndSendIt(photoFile: File) {
        Log.e("photoFile",photoFile.toString())
        val requestFile =
            photoFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        multipartImage =
            MultipartBody.Part.createFormData("photo", photoFile.name, requestFile)
        Log.e("multipartImage",multipartImage.toString())

        captureViewModel.getReadDocument(multipartImage)
        getReadDocument()
    }

}