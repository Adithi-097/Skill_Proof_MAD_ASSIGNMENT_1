package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    private var initialLeft = 0
    private var initialTop = 0
    private var initialRight = 0
    private var initialBottom = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = findViewById<View>(android.R.id.content)

        initialLeft = contentView.paddingLeft
        initialTop = contentView.paddingTop
        initialRight = contentView.paddingRight
        initialBottom = contentView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            val ime = insets.getInsets(
                WindowInsetsCompat.Type.ime()
            )

            val displayCutout = insets.getInsets(
                WindowInsetsCompat.Type.displayCutout()
            )

            val topInset = maxOf(
                systemBars.top,
                displayCutout.top
            )

            val bottomInset = maxOf(
                systemBars.bottom,
                ime.bottom,
                displayCutout.bottom
            )

            view.setPadding(
                initialLeft,
                initialTop + topInset,
                initialRight,
                initialBottom + bottomInset
            )

            insets
        }

        ViewCompat.requestApplyInsets(contentView)
    }
}