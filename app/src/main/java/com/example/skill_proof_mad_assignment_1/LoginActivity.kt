package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity :  BaseActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var btnLogin: MaterialButton
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_login
        )

        // =========================================
        // FIND VIEWS
        // =========================================

        etEmail =
            findViewById(
                R.id.etEmail
            )

        etPassword =
            findViewById(
                R.id.etPassword
            )

        btnLogin =
            findViewById(
                R.id.btnLogin
            )

        tvRegister =
            findViewById(
                R.id.tvRegister
            )

        // =========================================
        // LOGIN
        // =========================================

        btnLogin.setOnClickListener {

            loginUser()
        }

        // =========================================
        // REGISTER
        // =========================================

        tvRegister.setOnClickListener {

            val intent =
                Intent(
                    this,
                    RegistrationActivity::class.java
                )

            startActivity(intent)
        }
    }

    // =============================================
    // LOGIN USER
    // =============================================

    private fun loginUser() {

        val email =
            etEmail.text
                .toString()
                .trim()

        val password =
            etPassword.text
                .toString()

        // =========================================
        // VALIDATION
        // =========================================

        if (email.isEmpty()) {

            etEmail.error =
                "Enter your email"

            etEmail.requestFocus()

            return
        }

        if (
            !android.util.Patterns
                .EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            etEmail.error =
                "Enter a valid email address"

            etEmail.requestFocus()

            return
        }

        if (password.isEmpty()) {

            etPassword.error =
                "Enter your password"

            etPassword.requestFocus()

            return
        }

        // =========================================
        // GET SAVED USER
        // =========================================

        val sharedPreferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val registeredEmail =
            sharedPreferences.getString(
                "user_email",
                ""
            ) ?: ""

        val registeredPassword =
            sharedPreferences.getString(
                "user_password",
                ""
            ) ?: ""

        // =========================================
        // CHECK ACCOUNT
        // =========================================

        if (registeredEmail.isEmpty()) {

            Toast.makeText(
                this,
                "No account found. Please register first.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (
            email != registeredEmail ||
            password != registeredPassword
        ) {

            Toast.makeText(
                this,
                "Invalid email or password.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // =========================================
        // LOGIN SUCCESS
        // =========================================

        sharedPreferences
            .edit()
            .putBoolean(
                "is_logged_in",
                true
            )
            .apply()

        Toast.makeText(
            this,
            "Login successful!",
            Toast.LENGTH_SHORT
        ).show()

        val intent =
            Intent(
                this,
                DashboardActivity::class.java
            )

        startActivity(intent)

        finish()
    }
}