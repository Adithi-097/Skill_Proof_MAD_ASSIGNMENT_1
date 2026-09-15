package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class GitHubVerificationActivity : AppCompatActivity() {

    private lateinit var etGithubUrl: TextInputEditText
    private lateinit var btnVerify: MaterialButton
    private lateinit var resultCard: View

    private lateinit var tvVerificationStatus: TextView
    private lateinit var tvRepository: TextView
    private lateinit var tvOwner: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var tvStars: TextView
    private lateinit var tvForks: TextView
    private lateinit var tvRepositoryDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_github_verification)

        // --------------------------------------------------
        // Find Views
        // --------------------------------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        etGithubUrl =
            findViewById(R.id.etGithubUrl)

        btnVerify =
            findViewById(R.id.btnVerify)

        resultCard =
            findViewById(R.id.resultCard)

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

        // --------------------------------------------------
        // Back
        // --------------------------------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // --------------------------------------------------
        // Verify
        // --------------------------------------------------

        btnVerify.setOnClickListener {

            val githubUrl =
                etGithubUrl.text.toString().trim()

            if (githubUrl.isEmpty()) {

                etGithubUrl.error =
                    "Enter a GitHub repository URL"

                etGithubUrl.requestFocus()

                return@setOnClickListener
            }

            val repository =
                extractRepository(githubUrl)

            if (repository == null) {

                etGithubUrl.error =
                    "Enter a valid GitHub repository URL"

                etGithubUrl.requestFocus()

                return@setOnClickListener
            }

            val owner = repository.first
            val repo = repository.second

            verifyRepository(owner, repo)
        }
    }

    // ======================================================
    // EXTRACT OWNER + REPOSITORY
    // ======================================================

    private fun extractRepository(
        githubUrl: String
    ): Pair<String, String>? {

        var url = githubUrl.trim()

        // Remove trailing slash
        url = url.removeSuffix("/")

        // Remove .git if present
        url = url.removeSuffix(".git")

        // --------------------------------------------------
        // Supported formats:
        //
        // https://github.com/user/repository
        // http://github.com/user/repository
        // github.com/user/repository
        // --------------------------------------------------

        val regex =
            Regex(
                """(?:https?://)?(?:www\.)?github\.com/([^/\s]+)/([^/\s]+)"""
            )

        val match =
            regex.find(url)

        if (match != null) {

            val owner =
                match.groupValues[1]

            val repo =
                match.groupValues[2]

            return Pair(owner, repo)
        }

        return null
    }

    // ======================================================
    // VERIFY REPOSITORY
    // ======================================================

    private fun verifyRepository(
        owner: String,
        repo: String
    ) {

        // --------------------------------------------------
        // Disable button while checking
        // --------------------------------------------------

        btnVerify.isEnabled = false
        btnVerify.text = "Verifying..."

        resultCard.visibility = View.GONE

        // --------------------------------------------------
        // Run network request on background thread
        // --------------------------------------------------

        thread {

            var connection: HttpURLConnection? = null

            try {

                val apiUrl =
                    "https://api.github.com/repos/$owner/$repo"

                val url =
                    URL(apiUrl)

                connection =
                    url.openConnection() as HttpURLConnection

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

                // ==================================================
                // SUCCESS
                // ==================================================

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val response =
                        connection.inputStream
                            .bufferedReader()
                            .use {
                                it.readText()
                            }

                    val json =
                        JSONObject(response)

                    val repositoryName =
                        json.optString(
                            "name",
                            repo
                        )

                    val repositoryOwner =
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
                            "No description available."
                        )

                    runOnUiThread {

                        showVerifiedRepository(
                            repositoryName,
                            repositoryOwner,
                            language,
                            stars,
                            forks,
                            description
                        )

                        btnVerify.isEnabled = true
                        btnVerify.text = "Verify Repository"
                    }

                }

                // ==================================================
                // NOT FOUND
                // ==================================================

                else if (
                    responseCode ==
                    HttpURLConnection.HTTP_NOT_FOUND
                ) {

                    runOnUiThread {

                        showVerificationFailed(
                            "Repository not found. Check the GitHub URL."
                        )

                        btnVerify.isEnabled = true
                        btnVerify.text = "Verify Repository"
                    }

                }

                // ==================================================
                // RATE LIMIT
                // ==================================================

                else if (responseCode == 403) {

                    runOnUiThread {

                        showVerificationFailed(
                            "GitHub API rate limit reached. Try again later."
                        )

                        btnVerify.isEnabled = true
                        btnVerify.text = "Verify Repository"
                    }

                }

                // ==================================================
                // OTHER ERROR
                // ==================================================

                else {

                    runOnUiThread {

                        showVerificationFailed(
                            "Unable to verify repository. HTTP $responseCode"
                        )

                        btnVerify.isEnabled = true
                        btnVerify.text = "Verify Repository"
                    }
                }

            } catch (e: Exception) {

                runOnUiThread {

                    showVerificationFailed(
                        "Network error. Please check your internet connection."
                    )

                    btnVerify.isEnabled = true
                    btnVerify.text = "Verify Repository"

                    Toast.makeText(
                        this,
                        e.message ?: "Network error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } finally {

                connection?.disconnect()
            }
        }
    }

    // ======================================================
    // SHOW VERIFIED RESULT
    // ======================================================

    private fun showVerifiedRepository(
        repositoryName: String,
        owner: String,
        language: String,
        stars: Int,
        forks: Int,
        description: String
    ) {

        resultCard.visibility =
            View.VISIBLE

        tvVerificationStatus.text =
            "✓ Repository Verified"

        tvRepository.text =
            "Repository: $repositoryName"

        tvOwner.text =
            "Owner: $owner"

        tvLanguage.text =
            "Language: $language"

        tvStars.text =
            "Stars: $stars"

        tvForks.text =
            "Forks: $forks"

        tvRepositoryDescription.text =
            "Description: $description"
    }

    // ======================================================
    // SHOW FAILURE
    // ======================================================

    private fun showVerificationFailed(
        message: String
    ) {

        resultCard.visibility =
            View.VISIBLE

        tvVerificationStatus.text =
            "✗ Verification Failed"

        tvRepository.text =
            message

        tvOwner.text =
            "Owner: -"

        tvLanguage.text =
            "Language: -"

        tvStars.text =
            "Stars: -"

        tvForks.text =
            "Forks: -"

        tvRepositoryDescription.text =
            "Please check the repository URL and try again."
    }
}