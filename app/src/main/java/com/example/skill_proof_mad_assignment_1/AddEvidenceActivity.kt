package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddEvidenceActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnSaveEvidence: MaterialButton
    private lateinit var btnChooseFile: MaterialButton

    private lateinit var etEvidenceTitle: TextInputEditText
    private lateinit var etEvidenceSkill: TextInputEditText
    private lateinit var etEvidenceType: TextInputEditText
    private lateinit var etEvidenceLink: TextInputEditText

    private lateinit var ivAttachmentPreview: ImageView
    private lateinit var tvSelectedFile: TextView
    private lateinit var tvAttachmentHint: TextView

    private var selectedAttachmentUri: Uri? = null

    // ------------------------------------------------
    // File Picker
    // ------------------------------------------------

    private val filePickerLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                selectedAttachmentUri = uri

                displaySelectedFile(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_evidence)

        // ------------------------------------------------
        // Database
        // ------------------------------------------------

        databaseHelper =
            DatabaseHelper(this)

        // ------------------------------------------------
        // Find Views
        // ------------------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnSaveEvidence =
            findViewById(R.id.btnSaveEvidence)

        btnChooseFile =
            findViewById(R.id.btnChooseFile)

        etEvidenceTitle =
            findViewById(R.id.etEvidenceTitle)

        etEvidenceSkill =
            findViewById(R.id.etEvidenceSkill)

        etEvidenceType =
            findViewById(R.id.etEvidenceType)

        etEvidenceLink =
            findViewById(R.id.etEvidenceLink)

        ivAttachmentPreview =
            findViewById(R.id.ivAttachmentPreview)

        tvSelectedFile =
            findViewById(R.id.tvSelectedFile)

        tvAttachmentHint =
            findViewById(R.id.tvAttachmentHint)

        // ------------------------------------------------
        // Back
        // ------------------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        // ------------------------------------------------
        // Choose Attachment
        // ------------------------------------------------

        btnChooseFile.setOnClickListener {

            filePickerLauncher.launch("*/*")
        }

        // ------------------------------------------------
        // Save Evidence
        // ------------------------------------------------

        btnSaveEvidence.setOnClickListener {

            saveEvidence()
        }
    }

    // ------------------------------------------------
    // Save Evidence
    // ------------------------------------------------

    private fun saveEvidence() {

        val title =
            etEvidenceTitle.text
                .toString()
                .trim()

        val skill =
            etEvidenceSkill.text
                .toString()
                .trim()

        val type =
            etEvidenceType.text
                .toString()
                .trim()

        val link =
            etEvidenceLink.text
                .toString()
                .trim()

        // ------------------------------------------------
        // Title Validation
        // ------------------------------------------------

        if (title.isEmpty()) {

            etEvidenceTitle.error =
                "Enter an evidence title"

            etEvidenceTitle.requestFocus()

            return
        }

        // ------------------------------------------------
        // Skill Validation
        // ------------------------------------------------

        if (skill.isEmpty()) {

            etEvidenceSkill.error =
                "Enter the related skill"

            etEvidenceSkill.requestFocus()

            return
        }

        // ------------------------------------------------
        // Type Validation
        // ------------------------------------------------

        if (type.isEmpty()) {

            etEvidenceType.error =
                "Enter the evidence type"

            etEvidenceType.requestFocus()

            return
        }

        // ------------------------------------------------
        // Link Validation
        // ------------------------------------------------

        if (link.isEmpty()) {

            etEvidenceLink.error =
                "Enter a project, certificate or evidence link"

            etEvidenceLink.requestFocus()

            return
        }

        // ------------------------------------------------
        // Basic URL Validation
        // ------------------------------------------------

        val formattedLink =
            if (
                link.startsWith("http://") ||
                link.startsWith("https://")
            ) {
                link
            } else {
                "https://$link"
            }

        // ------------------------------------------------
        // Create Evidence
        // ------------------------------------------------

        val evidence =
            Evidence(
                title = title,
                skill = skill,
                type = type,
                link = formattedLink,
                status = "Not verified",
                attachmentUri =
                    selectedAttachmentUri?.toString()
                        ?: ""
            )

        // ------------------------------------------------
        // Save To Database
        // ------------------------------------------------

        databaseHelper.insertEvidence(evidence)

        // ------------------------------------------------
        // Success
        // ------------------------------------------------

        Toast.makeText(
            this,
            "Evidence added successfully!",
            Toast.LENGTH_SHORT
        ).show()

        // ------------------------------------------------
        // Return To Evidence
        // ------------------------------------------------

        finish()
    }

    // ------------------------------------------------
    // Display Selected File
    // ------------------------------------------------

    private fun displaySelectedFile(uri: Uri) {

        val fileName =
            getFileName(uri)

        tvSelectedFile.text =
            fileName

        tvSelectedFile.visibility =
            View.VISIBLE

        tvAttachmentHint.text =
            "Attachment selected"

        // ------------------------------------------------
        // Show Image Preview If Possible
        // ------------------------------------------------

        val mimeType =
            contentResolver.getType(uri)

        if (
            mimeType != null &&
            mimeType.startsWith("image/")
        ) {

            try {

                ivAttachmentPreview.setImageURI(uri)

                ivAttachmentPreview.visibility =
                    View.VISIBLE

            } catch (e: Exception) {

                ivAttachmentPreview.visibility =
                    View.GONE
            }

        } else {

            ivAttachmentPreview.visibility =
                View.GONE
        }
    }

    // ------------------------------------------------
    // Get File Name
    // ------------------------------------------------

    private fun getFileName(uri: Uri): String {

        var result: String? = null

        if (uri.scheme == "content") {

            val cursor =
                contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                )

            cursor?.use {

                if (it.moveToFirst()) {

                    val nameIndex =
                        it.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                        )

                    if (nameIndex >= 0) {

                        result =
                            it.getString(nameIndex)
                    }
                }
            }
        }

        if (result == null) {

            result =
                uri.path
                    ?.substringAfterLast('/')
        }

        return result ?: "Selected file"
    }
}