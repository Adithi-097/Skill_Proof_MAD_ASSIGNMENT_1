package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class AddEvidenceActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private var selectedImageUri: Uri? = null
    private var cameraImageUri: Uri? = null

    // ==========================================
    // GALLERY
    // ==========================================

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                selectedImageUri = uri

                val imagePreview =
                    findViewById<ImageView>(
                        R.id.ivAttachmentPreview
                    )

                imagePreview.setImageURI(uri)

                imagePreview.visibility =
                    ImageView.VISIBLE
            }
        }

    // ==========================================
    // CAMERA
    // ==========================================

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->

            if (success && cameraImageUri != null) {

                selectedImageUri =
                    cameraImageUri

                val imagePreview =
                    findViewById<ImageView>(
                        R.id.ivAttachmentPreview
                    )

                imagePreview.setImageURI(
                    cameraImageUri
                )

                imagePreview.visibility =
                    ImageView.VISIBLE

                Toast.makeText(
                    this,
                    "Photo captured successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Photo capture cancelled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_add_evidence
        )

        databaseHelper =
            DatabaseHelper(this)

        // ==========================================
        // FIND VIEWS
        // ==========================================

        val btnBack =
            findViewById<ImageButton>(
                R.id.btnBack
            )

        val etTitle =
            findViewById<TextInputEditText>(
                R.id.etTitle
            )

        val actvSkill =
            findViewById<AutoCompleteTextView>(
                R.id.actvSkill
            )

        val actvType =
            findViewById<AutoCompleteTextView>(
                R.id.actvType
            )

        val etLink =
            findViewById<TextInputEditText>(
                R.id.etLink
            )

        val btnCamera =
            findViewById<MaterialButton>(
                R.id.btnCamera
            )

        val btnGallery =
            findViewById<MaterialButton>(
                R.id.btnGallery
            )

        val btnSaveEvidence =
            findViewById<MaterialButton>(
                R.id.btnSaveEvidence
            )

        val imagePreview =
            findViewById<ImageView>(
                R.id.ivAttachmentPreview
            )

        // ==========================================
        // LOAD SKILLS FROM SQLITE
        // ==========================================

        val skillList =
            databaseHelper.getAllSkills()

        val skillNames =
            skillList
                .map { it.name }
                .toTypedArray()

        if (skillNames.isEmpty()) {

            Toast.makeText(
                this,
                "Please add a skill first",
                Toast.LENGTH_SHORT
            ).show()
        }

        val skillAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                skillNames
            )

        actvSkill.setAdapter(
            skillAdapter
        )

        // ==========================================
        // EVIDENCE TYPES
        // ==========================================

        val types =
            arrayOf(
                "Project",
                "Certificate",
                "GitHub Repository",
                "Other"
            )

        val typeAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                types
            )

        actvType.setAdapter(
            typeAdapter
        )

        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener {
            finish()
        }

        // ==========================================
        // CAMERA
        // ==========================================

        btnCamera.setOnClickListener {

            try {

                createCameraImageUri()

            } catch (e: Exception) {

                Toast.makeText(
                    this,
                    "Unable to open camera",
                    Toast.LENGTH_SHORT
                ).show()

                e.printStackTrace()
            }
        }

        // ==========================================
        // GALLERY
        // ==========================================

        btnGallery.setOnClickListener {

            galleryLauncher.launch(
                "image/*"
            )
        }

        // ==========================================
        // SAVE EVIDENCE
        // ==========================================

        btnSaveEvidence.setOnClickListener {

            val title =
                etTitle.text
                    .toString()
                    .trim()

            val skill =
                actvSkill.text
                    .toString()
                    .trim()

            val type =
                actvType.text
                    .toString()
                    .trim()

            val link =
                etLink.text
                    .toString()
                    .trim()

            // ======================================
            // VALIDATION
            // ======================================

            if (title.isEmpty()) {

                etTitle.error =
                    "Enter evidence title"

                etTitle.requestFocus()

                return@setOnClickListener
            }

            if (skill.isEmpty()) {

                actvSkill.error =
                    "Select a skill"

                actvSkill.requestFocus()

                return@setOnClickListener
            }

            if (type.isEmpty()) {

                actvType.error =
                    "Select evidence type"

                actvType.requestFocus()

                return@setOnClickListener
            }

            if (link.isEmpty()) {

                etLink.error =
                    "Enter a link"

                etLink.requestFocus()

                return@setOnClickListener
            }

            // ======================================
            // ATTACHMENT URI
            // ======================================

            val attachmentUri =
                selectedImageUri
                    ?.toString()
                    ?: ""

            // ======================================
            // RETURN DATA
            // ======================================

            val resultIntent =
                Intent()

            resultIntent.putExtra(
                "evidence_title",
                title
            )

            resultIntent.putExtra(
                "evidence_skill",
                skill
            )

            resultIntent.putExtra(
                "evidence_type",
                type
            )

            resultIntent.putExtra(
                "evidence_link",
                link
            )

            resultIntent.putExtra(
                "evidence_status",
                "Pending"
            )

            resultIntent.putExtra(
                "evidence_attachment_uri",
                attachmentUri
            )

            setResult(
                RESULT_OK,
                resultIntent
            )

            finish()
        }
    }

    // ==========================================
    // CREATE CAMERA IMAGE URI
    // ==========================================

    private fun createCameraImageUri() {

        val imageDirectory =
            getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            )

        if (imageDirectory == null) {

            Toast.makeText(
                this,
                "Unable to access picture directory",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Make sure directory exists
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs()
        }

        val imageFile =
            File.createTempFile(
                "skillproof_",
                ".jpg",
                imageDirectory
            )

        cameraImageUri =
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                imageFile
            )

        cameraLauncher.launch(
            cameraImageUri!!
        )
    }
}