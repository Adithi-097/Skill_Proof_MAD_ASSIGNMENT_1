package com.example.skill_proof_mad_assignment_1

data class Evidence(
    val title: String,
    val skill: String,
    val type: String,
    val link: String,
    val status: String,
    val attachmentUri: String = ""
)