package com.example.skill_proof_mad_assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {

        private const val DATABASE_NAME =
            "skillproof.db"

        private const val DATABASE_VERSION =
            2


        // =========================
        // SKILLS TABLE
        // =========================

        const val TABLE_SKILLS =
            "skills"

        const val COL_SKILL_ID =
            "id"

        const val COL_SKILL_NAME =
            "name"

        const val COL_SKILL_LEVEL =
            "level"

        const val COL_SKILL_PROGRESS =
            "progress"

        const val COL_SKILL_PROOF_STATUS =
            "proof_status"


        // =========================
        // EVIDENCE TABLE
        // =========================

        const val TABLE_EVIDENCE =
            "evidence"

        const val COL_EVIDENCE_ID =
            "id"

        const val COL_EVIDENCE_TITLE =
            "title"

        const val COL_EVIDENCE_SKILL =
            "skill"

        const val COL_EVIDENCE_TYPE =
            "type"

        const val COL_EVIDENCE_LINK =
            "link"

        const val COL_EVIDENCE_STATUS =
            "status"


        // =========================
        // ASSESSMENT TABLE
        // =========================

        const val TABLE_ASSESSMENTS =
            "assessments"

        const val COL_ASSESSMENT_ID =
            "id"

        const val COL_ASSESSMENT_SKILL =
            "skill"

        const val COL_ASSESSMENT_SCORE =
            "score"

        const val COL_ASSESSMENT_TOTAL =
            "total"

        const val COL_ASSESSMENT_PERCENTAGE =
            "percentage"
    }


    // =========================
    // CREATE DATABASE
    // =========================

    override fun onCreate(
        db: SQLiteDatabase
    ) {

        // -------------------------
        // SKILLS TABLE
        // -------------------------

        val createSkillsTable = """
            CREATE TABLE $TABLE_SKILLS (
                $COL_SKILL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_SKILL_NAME TEXT NOT NULL,
                $COL_SKILL_LEVEL TEXT NOT NULL,
                $COL_SKILL_PROGRESS INTEGER NOT NULL,
                $COL_SKILL_PROOF_STATUS TEXT NOT NULL
            )
        """.trimIndent()


        // -------------------------
        // EVIDENCE TABLE
        // -------------------------

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


        // -------------------------
        // ASSESSMENT TABLE
        // -------------------------

        val createAssessmentTable = """
            CREATE TABLE $TABLE_ASSESSMENTS (
                $COL_ASSESSMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ASSESSMENT_SKILL TEXT NOT NULL,
                $COL_ASSESSMENT_SCORE INTEGER NOT NULL,
                $COL_ASSESSMENT_TOTAL INTEGER NOT NULL,
                $COL_ASSESSMENT_PERCENTAGE INTEGER NOT NULL
            )
        """.trimIndent()


        // CREATE ALL TABLES

        db.execSQL(
            createSkillsTable
        )

        db.execSQL(
            createEvidenceTable
        )

        db.execSQL(
            createAssessmentTable
        )
    }


    // =========================
    // DATABASE UPGRADE
    // =========================

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_SKILLS"
        )

        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_EVIDENCE"
        )

        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_ASSESSMENTS"
        )

        onCreate(db)
    }


    // =====================================================
    // SKILLS
    // =====================================================

    // INSERT SKILL

    fun insertSkill(
        name: String,
        level: String,
        progress: Int,
        proofStatus: String
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues().apply {

                put(
                    COL_SKILL_NAME,
                    name
                )

                put(
                    COL_SKILL_LEVEL,
                    level
                )

                put(
                    COL_SKILL_PROGRESS,
                    progress
                )

                put(
                    COL_SKILL_PROOF_STATUS,
                    proofStatus
                )
            }

        return db.insert(
            TABLE_SKILLS,
            null,
            values
        )
    }


    // GET ALL SKILLS

    fun getAllSkills():
            MutableList<Skill> {

        val skills =
            mutableListOf<Skill>()

        val db =
            readableDatabase

        val cursor =
            db.query(
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
                        it.getColumnIndexOrThrow(
                            COL_SKILL_NAME
                        )
                    )

                val level =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_SKILL_LEVEL
                        )
                    )

                val progress =
                    it.getInt(
                        it.getColumnIndexOrThrow(
                            COL_SKILL_PROGRESS
                        )
                    )

                val proofStatus =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_SKILL_PROOF_STATUS
                        )
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


    // =====================================================
    // EVIDENCE
    // =====================================================

    // INSERT EVIDENCE

    fun insertEvidence(
        title: String,
        skill: String,
        type: String,
        link: String,
        status: String
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues().apply {

                put(
                    COL_EVIDENCE_TITLE,
                    title
                )

                put(
                    COL_EVIDENCE_SKILL,
                    skill
                )

                put(
                    COL_EVIDENCE_TYPE,
                    type
                )

                put(
                    COL_EVIDENCE_LINK,
                    link
                )

                put(
                    COL_EVIDENCE_STATUS,
                    status
                )
            }

        return db.insert(
            TABLE_EVIDENCE,
            null,
            values
        )
    }


    // GET ALL EVIDENCE

    fun getAllEvidence():
            MutableList<Evidence> {

        val evidenceList =
            mutableListOf<Evidence>()

        val db =
            readableDatabase

        val cursor =
            db.query(
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
                        it.getColumnIndexOrThrow(
                            COL_EVIDENCE_TITLE
                        )
                    )

                val skill =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_EVIDENCE_SKILL
                        )
                    )

                val type =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_EVIDENCE_TYPE
                        )
                    )

                val link =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_EVIDENCE_LINK
                        )
                    )

                val status =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            COL_EVIDENCE_STATUS
                        )
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


    // UPDATE EVIDENCE STATUS
    // Used for Pending -> Verified

    fun updateEvidenceStatus(
        title: String,
        status: String
    ): Int {

        val db =
            writableDatabase

        val values =
            ContentValues().apply {

                put(
                    COL_EVIDENCE_STATUS,
                    status
                )
            }

        return db.update(
            TABLE_EVIDENCE,
            values,
            "$COL_EVIDENCE_TITLE = ?",
            arrayOf(title)
        )
    }


    // =====================================================
    // ASSESSMENT
    // =====================================================

    // INSERT ASSESSMENT RESULT

    fun insertAssessment(
        skill: String,
        score: Int,
        total: Int,
        percentage: Int
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues().apply {

                put(
                    COL_ASSESSMENT_SKILL,
                    skill
                )

                put(
                    COL_ASSESSMENT_SCORE,
                    score
                )

                put(
                    COL_ASSESSMENT_TOTAL,
                    total
                )

                put(
                    COL_ASSESSMENT_PERCENTAGE,
                    percentage
                )
            }

        return db.insert(
            TABLE_ASSESSMENTS,
            null,
            values
        )
    }


    // GET LATEST ASSESSMENT PERCENTAGE

    fun getLatestAssessmentPercentage():
            Int {

        val db =
            readableDatabase

        val cursor =
            db.query(
                TABLE_ASSESSMENTS,
                arrayOf(
                    COL_ASSESSMENT_PERCENTAGE
                ),
                null,
                null,
                null,
                null,
                "$COL_ASSESSMENT_ID DESC",
                "1"
            )

        cursor.use {

            if (it.moveToFirst()) {

                return it.getInt(
                    it.getColumnIndexOrThrow(
                        COL_ASSESSMENT_PERCENTAGE
                    )
                )
            }
        }

        return 0
    }


    // =====================================================
    // PROOF SCORE
    // =====================================================

    // AVERAGE SKILL PROGRESS

    fun getAverageSkillProgress():
            Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                "SELECT AVG($COL_SKILL_PROGRESS) " +
                        "FROM $TABLE_SKILLS",
                null
            )

        cursor.use {

            if (
                it.moveToFirst() &&
                !it.isNull(0)
            ) {

                return it.getDouble(0).toInt()
            }
        }

        return 0
    }


    // EVIDENCE SCORE

    fun getEvidenceScore():
            Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                "SELECT COUNT(*) " +
                        "FROM $TABLE_EVIDENCE",
                null
            )

        cursor.use {

            if (it.moveToFirst()) {

                val count =
                    it.getInt(0)

                return when {

                    count >= 5 ->
                        100

                    count == 4 ->
                        90

                    count == 3 ->
                        80

                    count == 2 ->
                        65

                    count == 1 ->
                        45

                    else ->
                        0
                }
            }
        }

        return 0
    }


    // VERIFICATION SCORE

    fun getVerificationScore():
            Int {

        val db =
            readableDatabase


        // TOTAL EVIDENCE

        val totalCursor =
            db.rawQuery(
                "SELECT COUNT(*) " +
                        "FROM $TABLE_EVIDENCE",
                null
            )


        // VERIFIED EVIDENCE

        val verifiedCursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                WHERE $COL_EVIDENCE_STATUS = ?
                """.trimIndent(),
                arrayOf("Verified")
            )


        var total =
            0

        var verified =
            0


        totalCursor.use {

            if (it.moveToFirst()) {

                total =
                    it.getInt(0)
            }
        }


        verifiedCursor.use {

            if (it.moveToFirst()) {

                verified =
                    it.getInt(0)
            }
        }


        // NO EVIDENCE

        if (total == 0) {

            return 0
        }


        // CALCULATE VERIFICATION %

        return (
                verified * 100
                ) / total
    }
}