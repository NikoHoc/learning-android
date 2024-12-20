package com.dicoding.dicodingstory.view.addstory

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.databinding.ActivityAddStoryBinding
import com.dicoding.dicodingstory.utils.getImageUri
import com.dicoding.dicodingstory.utils.reduceFileImage
import com.dicoding.dicodingstory.utils.uriToFile
import com.dicoding.dicodingstory.view.ViewModelFactory
import com.dicoding.dicodingstory.view.main.MainActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonAdd.isEnabled = false
        binding.buttonAdd.backgroundTintList = getColorStateList(R.color.disabled_button)
        binding.buttonAdd.text = getString(R.string.description_empty)

        binding.edAddDescription.addTextChangedListener(formTextWatcher)
    }

    override fun onSupportNavigateUp(): Boolean {
        finishAfterTransition()
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAction() {
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    @SuppressLint("NewApi")
    private fun uploadImage() {

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@AddStoryActivity).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString()
            Log.d("Story Description", "description: $description")

            viewModel.uploadStory(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val uploadResponse = result.data
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.upload_success_alert_title)
                                setMessage(uploadResponse.message)
                                setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.upload_error_alert_title)
                                setMessage(result.error)
                                setPositiveButton(R.string.error_alert_reply) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                    }
                }
            }
        } ?: Toast.makeText(this, getString(R.string.image_empty), Toast.LENGTH_LONG).show()
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.previewImageView, View.ALPHA, 1F).setDuration(150)
        val descriptionLayout = ObjectAnimator.ofFloat(binding.edTextInputLayout, View.ALPHA, 1F).setDuration(150)
        val description = ObjectAnimator.ofFloat(binding.edAddDescription, View.ALPHA, 1F).setDuration(150)
        val galleryButton = ObjectAnimator.ofFloat(binding.galleryButton, View.ALPHA, 1F).setDuration(150)
        val cameraButton = ObjectAnimator.ofFloat(binding.cameraButton, View.ALPHA, 1F).setDuration(150)
        val uploadButton = ObjectAnimator.ofFloat(binding.buttonAdd, View.ALPHA, 1F).setDuration(150)

        AnimatorSet().apply {
            playSequentially(
                image, descriptionLayout, description, galleryButton, cameraButton, uploadButton
            )
            startDelay = 200
            start()
        }

    }

    private val formTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val isFormFilled = !binding.edAddDescription.text.isNullOrEmpty()
            binding.buttonAdd.isEnabled = isFormFilled

            if (binding.buttonAdd.isEnabled) {
                binding.buttonAdd.backgroundTintList = getColorStateList(R.color.md_theme_primary)
                binding.buttonAdd.text = getString(R.string.upload)
            } else {
                binding.buttonAdd.backgroundTintList = getColorStateList(R.color.disabled_button)
                binding.buttonAdd.text = getString(R.string.description_empty)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}