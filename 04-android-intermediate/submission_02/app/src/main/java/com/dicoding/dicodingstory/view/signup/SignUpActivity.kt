package com.dicoding.dicodingstory.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.dicodingstory.R
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.databinding.ActivitySignUpBinding
import com.dicoding.dicodingstory.view.ViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupButton.isEnabled = false
        binding.signupButton.backgroundTintList = getColorStateList(R.color.disabled_button)
        binding.signupButton.text = getString(R.string.message_register_page)

        setupView()
        setupAction()
        playAnimation()

        binding.edRegisterName.addTextChangedListener(formTextWatcher)
        binding.edRegisterEmail.addTextChangedListener(formTextWatcher)
        binding.edRegisterPassword.addTextChangedListener(formTextWatcher)
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
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.registerUser(name, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val signUpResponse = result.data
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.signup_success_alert_title)
                                setMessage(signUpResponse.message)
                                setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.signup_error_alert_title)
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
        }
    }

    private val formTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val isFormFilled = !binding.edRegisterName.text.isNullOrEmpty() &&
                    !binding.edRegisterEmail.text.isNullOrEmpty() &&
                    !binding.edRegisterPassword.text.isNullOrEmpty()

            binding.signupButton.isEnabled = isFormFilled &&
                    binding.edRegisterName.error == null &&
                    binding.edRegisterEmail.error == null &&
                    binding.edRegisterPassword.error == null

            if (binding.signupButton.isEnabled) {
                binding.signupButton.backgroundTintList = getColorStateList(R.color.md_theme_primary)
                binding.signupButton.text = getString(R.string.signup)
            } else {
                binding.signupButton.backgroundTintList = getColorStateList(R.color.disabled_button)
                binding.signupButton.text = getString(R.string.message_register_page)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 100
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        // muncul bergantian
        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
            start()
        }
    }

}