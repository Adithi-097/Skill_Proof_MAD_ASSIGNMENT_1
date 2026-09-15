package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class GitHubVerificationActivity : BaseActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnVerify: MaterialButton
    private lateinit var btnUseAsEvidence: MaterialButton

    private lateinit var etGithubUrl: TextInputEditText

    private lateinit var resultCard: View

    private lateinit var tvVerificationStatus: TextView
    private lateinit var tvRepository: TextView
    private lateinit var tvOwner: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var tvStars: TextView
    private lateinit var tvForks: TextView
    private lateinit var tvRepositoryDescription: TextView

    private var verifiedRepositoryUrl = ""
    private var verifiedRepositoryName = ""
    private var verifiedOwner = ""
    private var verifiedLanguage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_github_verification)

        // ------------------------------------------------
        // Find Views
        // ------------------------------------------------

        btnBack = findViewById(R.id.btnBack)
        btnVerify = findViewById(R.id.btnVerify)
        btnUseAsEvidence = findViewById(R.id.btnUseAsEvidence)

        etGithubUrl = findViewById(R.id.etGithubUrl)

        resultCard = findViewById(R.id.resultCard)

        tvVerificationStatus =
            findViewById(R.id.tvVerificationStatus)

        tvRepository =
            findViewById(R.id.tvRepository)

        tvOwner =
            findViewById(R.id.tvOwner)

        tvLanguage =
            findViewById(R.id.tvLanguage)

        tvStars =
            findViewById(R.id.tvStars)

        tvForks =
            findViewById(R.id.tvForks)

        tvRepositoryDescription =
            findViewById(R.id.tvRepositoryDescription)

        // ------------------------------------------------
        // Initial State
        // ------------------------------------------------

        resultCard.visibility = View.GONE
        btnUseAsEvidence.visibility = View.GONE

        // ------------------------------------------------
        // Back
        // ------------------------------------------------

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // ------------------------------------------------
        // Verify Repository
        // ------------------------------------------------

        btnVerify.setOnClickListener {
            verifyRepository()
        }

        // ------------------------------------------------
        // Use As Evidence
        // ------------------------------------------------

        btnUseAsEvidence.setOnClickListener {
            useRepositoryAsEvidence()
        }
    }

    // ====================================================
    // VERIFY REPOSITORY
    // ====================================================

    private fun verifyRepository() {

        val githubUrl =
            etGithubUrl.text?.toString()?.trim() ?: ""

        if (githubUrl.isEmpty()) {

            etGithubUrl.error =
                "Enter a GitHub repository URL"

            etGithubUrl.requestFocus()

            return
        }

        val repositoryInfo =
            extractRepositoryInfo(githubUrl)

        if (repositoryInfo == null) {

            etGithubUrl.error =
                "Enter a valid GitHub repository URL"

            etGithubUrl.requestFocus()

            return
        }

        val owner = repositoryInfo.first
        val repository = repositoryInfo.second

        // ------------------------------------------------
        // Loading state
        // ------------------------------------------------

        btnVerify.isEnabled = false
        btnVerify.text = "Verifying..."

        resultCard.visibility = View.GONE
        btnUseAsEvidence.visibility = View.GONE

        // ------------------------------------------------
        // GitHub API call
        // ------------------------------------------------

        thread {

            var connection: HttpURLConnection? = null

            try {

                val apiUrl =
                    URL(
                        "https://api.github.com/repos/$owner/$repository"
                    )

                connection =
                    apiUrl.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                connection.setRequestProperty(
                    "Accept",
                    "application/vnd.github+json"
                )

                connection.setRequestProperty(
                    "User-Agent",
                    "SkillProof-Android-App"
                )

                val responseCode =
                    connection.responseCode

                if (responseCode == 200) {

                    val response =
                        connection.inputStream
                            .bufferedReader()
                            .use { it.readText() }

                    val json =
                        JSONObject(response)

                    val name =
                        json.optString(
                            "name",
                            repository
                        )

                    val ownerLogin =
                        json.optJSONObject("owner")
                            ?.optString(
                                "login",
                                owner
                            )
                            ?: owner

                    val language =
                        json.optString(
                            "language",
                            "Not specified"
                        )

                    val stars =
                        json.optInt(
                            "stargazers_count",
                            0
                        )

                    val forks =
                        json.optInt(
                            "forks_count",
                            0
                        )

                    val description =
                        json.optString(
                            "description",
                            ""
                        )

                    runOnUiThread {

                        verifiedRepositoryUrl =
                            githubUrl

                        verifiedRepositoryName =
                            name

                        verifiedOwner =
                            ownerLogin

                        verifiedLanguage =
                            language

                        tvVerificationStatus.text =
                            "✓ Repository Verified"

                        tvRepository.text =
                            "Repository: $name"

                        tvOwner.text =
                            "Owner: $ownerLogin"

                        tvLanguage.text =
                            "Language: $language"

                        tvStars.text =
                            "Stars: $stars"

                        tvForks.text =
                            "Forks: $forks"

                        tvRepositoryDescription.text =
                            if (description.isBlank()) {
                                "No description available."
                            } else {
                                description
                            }

                        resultCard.visibility =
                            View.VISIBLE

                        btnUseAsEvidence.visibility =
                            View.VISIBLE

                        btnVerify.isEnabled =
                            true

                        btnVerify.text =
                            "Verify Repository"

                        Toast.makeText(
                            this,
                            "GitHub repository verified.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    runOnUiThread {

                        showVerificationError(
                            responseCode
                        )
                    }
                }

            } catch (e: Exception) {

                runOnUiThread {

                    resultCard.visibility =
                        View.VISIBLE

                    btnUseAsEvidence.visibility =
                        View.GONE

                    tvVerificationStatus.text =
                        "Verification Failed"

                    tvRepository.text =
                        "Unable to connect to GitHub."

                    tvOwner.text = ""
                    tvLanguage.text = ""
                    tvStars.text = ""
                    tvForks.text = ""

                    tvRepositoryDescription.text =
                        "Please check your internet connection and try again."

                    btnVerify.isEnabled =
                        true

                    btnVerify.text =
                        "Verify Repository"

                    Toast.makeText(
                        this,
                        "Unable to connect to GitHub.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } finally {

                connection?.disconnect()
            }
        }
    }

    // ====================================================
    // EXTRACT OWNER / REPOSITORY
    // ====================================================

    private fun extractRepositoryInfo(
        url: String
    ): Pair<String, String>? {

        val cleanedUrl =
            url
                .trim()
                .removeSuffix("/")

        val regex =
            Regex(
                """(?:https?://)?(?:www\.)?github\.com/([^/\s]+)/([^/\s#?]+)"""
            )

        val match =
            regex.find(cleanedUrl)
                ?: return null

        val owner =
            match.groupValues[1]

        val repository =
            match.groupValues[2]
                .removeSuffix(".git")

        if (
            owner.isBlank() ||
            repository.isBlank()
        ) {
            return null
        }

        return Pair(
            owner,
            repository
        )
    }

    // ====================================================
    // ERROR
    // ====================================================

    private fun showVerificationError(
        responseCode: Int
    ) {

        resultCard.visibility =
            View.VISIBLE

        btnUseAsEvidence.visibility =
            View.GONE

        when (responseCode) {

            404 -> {

                tvVerificationStatus.text =
                    "Repository Not Found"

                tvRepository.text =
                    "The repository does not exist or is private."
            }

            403 -> {

                tvVerificationStatus.text =
                    "GitHub API Limit Reached"

                tvRepository.text =
                    "Please try again later."
            }

            else -> {

                tvVerificationStatus.text =
                    "Verification Failed"

                tvRepository.text =
                    "GitHub returned error code $responseCode."
            }
        }

        tvOwner.text = ""
        tvLanguage.text = ""
        tvStars.text = ""
        tvForks.text = ""
        tvRepositoryDescription.text = ""

        btnVerify.isEnabled = true
        btnVerify.text = "Verify Repository"
    }

    // ====================================================
    // USE AS VERIFIED EVIDENCE
    // ====================================================

    private fun useRepositoryAsEvidence() {

        if (verifiedRepositoryName.isBlank()) {

            Toast.makeText(
                this,
                "Please verify a repository first.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val resultIntent =
            Intent()

        resultIntent.putExtra(
            "github_verified",
            true
        )

        resultIntent.putExtra(
            "github_owner",
            verifiedOwner
        )

        resultIntent.putExtra(
            "github_repository",
            verifiedRepositoryName
        )

        resultIntent.putExtra(
            "github_language",
            verifiedLanguage
        )

        resultIntent.putExtra(
            "github_url",
            verifiedRepositoryUrl
        )

        setResult(
            RESULT_OK,
            resultIntent
        )

        finish()
    }
}