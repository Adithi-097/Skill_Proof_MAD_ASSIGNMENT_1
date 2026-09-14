package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registration)

        val etName =
            findViewById<TextInputEditText>(R.id.etName)

        val etEmail =
            findViewById<TextInputEditText>(R.id.etEmail)

        val etPassword =
            findViewById<TextInputEditText>(R.id.etPassword)

        val etConfirmPassword =
            findViewById<TextInputEditText>(R.id.etConfirmPassword)

        val btnRegister =
            findViewById<MaterialButton>(R.id.btnRegister)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val tvLogin =
            findViewById<TextView>(R.id.tvLogin)

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (name.isEmpty()) {
                etName.error = "Enter your name"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Enter your email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()
            ) {
                etEmail.error = "Enter a valid email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Enter a password"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error =
                    "Password must contain at least 6 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error =
                    "Confirm your password"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etConfirmPassword.error =
                    "Passwords do not match"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Account created successfully!",
                Toast.LENGTH_SHORT
            ).show()

            val sharedPreferences =
                getSharedPreferences(
                    "SkillProofPrefs",
                    MODE_PRIVATE
                )

            sharedPreferences.edit()
                .putString("user_name", name)
                .putString("user_email", email)
                .apply()

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }
}