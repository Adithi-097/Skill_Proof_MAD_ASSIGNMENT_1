package com.example.skill_proof_mad_assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "skillproof.db"
        private const val DATABASE_VERSION = 1

        // Skills table
        const val TABLE_SKILLS = "skills"
        const val COL_SKILL_ID = "id"
        const val COL_SKILL_NAME = "name"
        const val COL_SKILL_LEVEL = "level"
        const val COL_SKILL_PROGRESS = "progress"
        const val COL_SKILL_PROOF_STATUS = "proof_status"

        // Evidence table
        const val TABLE_EVIDENCE = "evidence"
        const val COL_EVIDENCE_ID = "id"
        const val COL_EVIDENCE_TITLE = "title"
        const val COL_EVIDENCE_SKILL = "skill"
        const val COL_EVIDENCE_TYPE = "type"
        const val COL_EVIDENCE_LINK = "link"
        const val COL_EVIDENCE_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createSkillsTable = """
            CREATE TABLE $TABLE_SKILLS (
                $COL_SKILL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_SKILL_NAME TEXT NOT NULL,
                $COL_SKILL_LEVEL TEXT NOT NULL,
                $COL_SKILL_PROGRESS INTEGER NOT NULL,
                $COL_SKILL_PROOF_STATUS TEXT NOT NULL
            )
        """.trimIndent()

        val createEvidenceTable = """
            CREATE TABLE $TABLE_EVIDENCE (
                $COL_EVIDENCE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EVIDENCE_TITLE TEXT NOT NULL,
                $COL_EVIDENCE_SKILL TEXT NOT NULL,
                $COL_EVIDENCE_TYPE TEXT NOT NULL,
                $COL_EVIDENCE_LINK TEXT NOT NULL,
                $COL_EVIDENCE_STATUS TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createSkillsTable)
        db.execSQL(createEvidenceTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SKILLS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVIDENCE")
        onCreate(db)
    }

    fun insertSkill(
        name: String,
        level: String,
        progress: Int,
        proofStatus: String
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COL_SKILL_NAME, name)
            put(COL_SKILL_LEVEL, level)
            put(COL_SKILL_PROGRESS, progress)
            put(COL_SKILL_PROOF_STATUS, proofStatus)
        }

        return db.insert(
            TABLE_SKILLS,
            null,
            values
        )
    }

    fun getAllSkills(): MutableList<Skill> {

        val skills = mutableListOf<Skill>()

        val db = readableDatabase

        val cursor = db.query(
            TABLE_SKILLS,
            null,
            null,
            null,
            null,
            null,
            "$COL_SKILL_ID DESC"
        )

        cursor.use {

            while (it.moveToNext()) {

                val name =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_SKILL_NAME)
                    )

                val level =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_SKILL_LEVEL)
                    )

                val progress =
                    it.getInt(
                        it.getColumnIndexOrThrow(COL_SKILL_PROGRESS)
                    )

                val proofStatus =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_SKILL_PROOF_STATUS)
                    )

                skills.add(
                    Skill(
                        name,
                        level,
                        progress,
                        proofStatus
                    )
                )
            }
        }

        return skills
    }

    fun insertEvidence(
        title: String,
        skill: String,
        type: String,
        link: String,
        status: String
    ): Long {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COL_EVIDENCE_TITLE, title)
            put(COL_EVIDENCE_SKILL, skill)
            put(COL_EVIDENCE_TYPE, type)
            put(COL_EVIDENCE_LINK, link)
            put(COL_EVIDENCE_STATUS, status)
        }

        return db.insert(
            TABLE_EVIDENCE,
            null,
            values
        )
    }

    fun getAllEvidence(): MutableList<Evidence> {

        val evidenceList = mutableListOf<Evidence>()

        val db = readableDatabase

        val cursor = db.query(
            TABLE_EVIDENCE,
            null,
            null,
            null,
            null,
            null,
            "$COL_EVIDENCE_ID DESC"
        )

        cursor.use {

            while (it.moveToNext()) {

                val title =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_EVIDENCE_TITLE)
                    )

                val skill =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_EVIDENCE_SKILL)
                    )

                val type =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_EVIDENCE_TYPE)
                    )

                val link =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_EVIDENCE_LINK)
                    )

                val status =
                    it.getString(
                        it.getColumnIndexOrThrow(COL_EVIDENCE_STATUS)
                    )

                evidenceList.add(
                    Evidence(
                        title,
                        skill,
                        type,
                        link,
                        status
                    )
                )
            }
        }

        return evidenceList
    }
}