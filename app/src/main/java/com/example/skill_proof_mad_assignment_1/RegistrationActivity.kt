package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegistrationActivity :  BaseActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText

    private lateinit var btnRegister: MaterialButton
    private lateinit var btnBack: ImageButton
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IMPORTANT:
        // The actual XML file in the project is activity_registration.xml
        setContentView(R.layout.activity_registration)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnRegister = findViewById(R.id.btnRegister)
        btnBack = findViewById(R.id.btnBack)
        tvLogin = findViewById(R.id.tvLogin)

        // Back button
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Register button
        btnRegister.setOnClickListener {
            registerUser()
        }

        // Already have an account
        tvLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun registerUser() {

        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        // -----------------------------
        // NAME VALIDATION
        // -----------------------------

        if (name.isEmpty()) {
            etName.error = "Enter your name"
            etName.requestFocus()
            return
        }

        // -----------------------------
        // EMAIL VALIDATION
        // -----------------------------

        if (email.isEmpty()) {
            etEmail.error = "Enter your email"
            etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email address"
            etEmail.requestFocus()
            return
        }

        // -----------------------------
        // PASSWORD VALIDATION
        // -----------------------------

        if (password.isEmpty()) {
            etPassword.error = "Enter a password"
            etPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error =
                "Password must contain at least 6 characters"
            etPassword.requestFocus()
            return
        }

        // -----------------------------
        // CONFIRM PASSWORD VALIDATION
        // -----------------------------

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Confirm your password"
            etConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            etConfirmPassword.requestFocus()
            return
        }

        // -----------------------------
        // SAVE USER DATA
        // -----------------------------

        val preferences =
            getSharedPreferences("SkillProofPrefs", MODE_PRIVATE)

        preferences.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_password", password)
            .putBoolean("is_logged_in", false)
            .apply()

        // -----------------------------
        // SUCCESS MESSAGE
        // -----------------------------

        Toast.makeText(
            this,
            "Account created successfully!",
            Toast.LENGTH_SHORT
        ).show()

        // -----------------------------
        // GO TO LOGIN
        // -----------------------------

        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        startActivity(intent)

        // Prevent going back to registration
        finish()
    }
}