package com.example.skill_proof_mad_assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {

        private const val DATABASE_NAME =
            "SkillProof.db"

        private const val DATABASE_VERSION =
            3

        // =========================================
        // SKILLS TABLE
        // =========================================

        private const val TABLE_SKILLS =
            "skills"

        private const val COL_SKILL_ID =
            "id"

        private const val COL_SKILL_NAME =
            "name"

        private const val COL_SKILL_LEVEL =
            "level"

        private const val COL_SKILL_PROGRESS =
            "progress"

        private const val COL_SKILL_PROOF_STATUS =
            "proof_status"

        // =========================================
        // EVIDENCE TABLE
        // =========================================

        private const val TABLE_EVIDENCE =
            "evidence"

        private const val COL_EVIDENCE_ID =
            "id"

        private const val COL_EVIDENCE_TITLE =
            "title"

        private const val COL_EVIDENCE_SKILL =
            "skill"

        private const val COL_EVIDENCE_TYPE =
            "type"

        private const val COL_EVIDENCE_LINK =
            "link"

        private const val COL_EVIDENCE_STATUS =
            "status"

        private const val COL_EVIDENCE_ATTACHMENT =
            "attachment_uri"

        // =========================================
        // ASSESSMENT TABLE
        // =========================================

        private const val TABLE_ASSESSMENTS =
            "assessments"

        private const val COL_ASSESSMENT_ID =
            "id"

        private const val COL_ASSESSMENT_SKILL =
            "skill"

        private const val COL_ASSESSMENT_SCORE =
            "score"

        private const val COL_ASSESSMENT_TOTAL =
            "total"

        private const val COL_ASSESSMENT_PERCENTAGE =
            "percentage"
    }

    // =====================================================
    // CREATE DATABASE
    // =====================================================

    override fun onCreate(
        db: SQLiteDatabase
    ) {

        // =========================================
        // SKILLS TABLE
        // =========================================

        val createSkillsTable = """
            CREATE TABLE $TABLE_SKILLS (
                $COL_SKILL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_SKILL_NAME TEXT NOT NULL,
                $COL_SKILL_LEVEL TEXT NOT NULL,
                $COL_SKILL_PROGRESS INTEGER NOT NULL,
                $COL_SKILL_PROOF_STATUS TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(
            createSkillsTable
        )

        // =========================================
        // EVIDENCE TABLE
        // =========================================

        val createEvidenceTable = """
            CREATE TABLE $TABLE_EVIDENCE (
                $COL_EVIDENCE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EVIDENCE_TITLE TEXT NOT NULL,
                $COL_EVIDENCE_SKILL TEXT NOT NULL,
                $COL_EVIDENCE_TYPE TEXT NOT NULL,
                $COL_EVIDENCE_LINK TEXT NOT NULL,
                $COL_EVIDENCE_STATUS TEXT NOT NULL,
                $COL_EVIDENCE_ATTACHMENT TEXT DEFAULT ''
            )
        """.trimIndent()

        db.execSQL(
            createEvidenceTable
        )

        // =========================================
        // ASSESSMENTS TABLE
        // =========================================

        val createAssessmentTable = """
            CREATE TABLE $TABLE_ASSESSMENTS (
                $COL_ASSESSMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ASSESSMENT_SKILL TEXT NOT NULL,
                $COL_ASSESSMENT_SCORE INTEGER NOT NULL,
                $COL_ASSESSMENT_TOTAL INTEGER NOT NULL,
                $COL_ASSESSMENT_PERCENTAGE INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(
            createAssessmentTable
        )
    }

    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        if (oldVersion < 2) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS $TABLE_ASSESSMENTS (
                    $COL_ASSESSMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COL_ASSESSMENT_SKILL TEXT NOT NULL,
                    $COL_ASSESSMENT_SCORE INTEGER NOT NULL,
                    $COL_ASSESSMENT_TOTAL INTEGER NOT NULL,
                    $COL_ASSESSMENT_PERCENTAGE INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }

        if (oldVersion < 3) {

            try {

                db.execSQL(
                    """
                    ALTER TABLE $TABLE_EVIDENCE
                    ADD COLUMN $COL_EVIDENCE_ATTACHMENT TEXT DEFAULT ''
                    """.trimIndent()
                )

            } catch (
                e: Exception
            ) {
                // Column may already exist.
            }
        }
    }

    // =====================================================
    // SKILL METHODS
    // =====================================================

    fun insertSkill(
        skill: Skill
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues()

        values.put(
            COL_SKILL_NAME,
            skill.name
        )

        values.put(
            COL_SKILL_LEVEL,
            skill.level
        )

        values.put(
            COL_SKILL_PROGRESS,
            skill.progress
        )

        values.put(
            COL_SKILL_PROOF_STATUS,
            skill.proofStatus
        )

        val result =
            db.insert(
                TABLE_SKILLS,
                null,
                values
            )

        db.close()

        return result
    }

    fun getAllSkills(): List<Skill> {

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
                "$COL_SKILL_ID ASC"
            )

        cursor.use {

            while (it.moveToNext()) {

                val skill =
                    Skill(
                        name =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_NAME
                                )
                            ),

                        level =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_LEVEL
                                )
                            ),

                        progress =
                            it.getInt(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_PROGRESS
                                )
                            ),

                        proofStatus =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_PROOF_STATUS
                                )
                            )
                    )

                skills.add(skill)
            }
        }

        db.close()

        return skills
    }

    // =====================================================
    // GET SKILL BY NAME
    // =====================================================

    fun getSkillByName(
        skillName: String
    ): Skill? {

        val db =
            readableDatabase

        val cursor =
            db.query(
                TABLE_SKILLS,
                null,
                "$COL_SKILL_NAME = ?",
                arrayOf(skillName),
                null,
                null,
                "$COL_SKILL_ID DESC",
                "1"
            )

        var skill: Skill? = null

        cursor.use {

            if (it.moveToFirst()) {

                skill =
                    Skill(
                        name =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_NAME
                                )
                            ),

                        level =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_LEVEL
                                )
                            ),

                        progress =
                            it.getInt(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_PROGRESS
                                )
                            ),

                        proofStatus =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_SKILL_PROOF_STATUS
                                )
                            )
                    )
            }
        }

        db.close()

        return skill
    }

    // =====================================================
    // EVIDENCE METHODS
    // =====================================================

    fun insertEvidence(
        evidence: Evidence
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues()

        values.put(
            COL_EVIDENCE_TITLE,
            evidence.title
        )

        values.put(
            COL_EVIDENCE_SKILL,
            evidence.skill
        )

        values.put(
            COL_EVIDENCE_TYPE,
            evidence.type
        )

        values.put(
            COL_EVIDENCE_LINK,
            evidence.link
        )

        values.put(
            COL_EVIDENCE_STATUS,
            evidence.status
        )

        values.put(
            COL_EVIDENCE_ATTACHMENT,
            evidence.attachmentUri
        )

        val result =
            db.insert(
                TABLE_EVIDENCE,
                null,
                values
            )

        db.close()

        return result
    }

    // =====================================================
    // GET ALL EVIDENCE
    // =====================================================

    fun getAllEvidence(): List<Evidence> {

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

                val evidence =
                    Evidence(
                        title =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_TITLE
                                )
                            ),

                        skill =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_SKILL
                                )
                            ),

                        type =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_TYPE
                                )
                            ),

                        link =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_LINK
                                )
                            ),

                        status =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_STATUS
                                )
                            ),

                        attachmentUri =
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    COL_EVIDENCE_ATTACHMENT
                                )
                            )
                    )

                evidenceList.add(
                    evidence
                )
            }
        }

        db.close()

        return evidenceList
    }

    // =====================================================
    // UPDATE EVIDENCE STATUS
    // =====================================================

    fun updateEvidenceStatus(
        link: String,
        newStatus: String
    ): Int {

        val db =
            writableDatabase

        val values =
            ContentValues()

        values.put(
            COL_EVIDENCE_STATUS,
            newStatus
        )

        val rowsUpdated =
            db.update(
                TABLE_EVIDENCE,
                values,
                "$COL_EVIDENCE_LINK = ?",
                arrayOf(link)
            )

        db.close()

        return rowsUpdated
    }

    // =====================================================
    // GET EVIDENCE COUNT
    // =====================================================

    fun getEvidenceCountForSkill(
        skillName: String
    ): Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                WHERE LOWER($COL_EVIDENCE_SKILL) = LOWER(?)
                """.trimIndent(),
                arrayOf(skillName)
            )

        var count = 0

        cursor.use {

            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }

        db.close()

        return count
    }

    // =====================================================
    // VERIFIED EVIDENCE COUNT
    // =====================================================

    fun getVerifiedEvidenceCountForSkill(
        skillName: String
    ): Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                WHERE LOWER($COL_EVIDENCE_SKILL) = LOWER(?)
                AND LOWER($COL_EVIDENCE_STATUS) = LOWER('Verified')
                """.trimIndent(),
                arrayOf(skillName)
            )

        var count = 0

        cursor.use {

            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }

        db.close()

        return count
    }

    // =====================================================
    // EVIDENCE SCORE
    // =====================================================

    fun getEvidenceScore(): Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                """.trimIndent(),
                null
            )

        var count = 0

        cursor.use {

            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }

        db.close()

        return calculateEvidenceScore(
            count
        )
    }

    private fun calculateEvidenceScore(
        count: Int
    ): Int {

        return when {

            count <= 0 ->
                0

            count == 1 ->
                45

            count == 2 ->
                65

            count == 3 ->
                80

            count == 4 ->
                90

            else ->
                100
        }
    }

    // =====================================================
    // VERIFICATION SCORE
    // =====================================================

    fun getVerificationScore(): Int {

        val db =
            readableDatabase

        val totalCursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                """.trimIndent(),
                null
            )

        var total = 0

        totalCursor.use {

            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }

        val verifiedCursor =
            db.rawQuery(
                """
                SELECT COUNT(*)
                FROM $TABLE_EVIDENCE
                WHERE LOWER($COL_EVIDENCE_STATUS)
                = LOWER('Verified')
                """.trimIndent(),
                null
            )

        var verified = 0

        verifiedCursor.use {

            if (it.moveToFirst()) {
                verified = it.getInt(0)
            }
        }

        db.close()

        if (total == 0) {
            return 0
        }

        return (
                verified * 100
                ) / total
    }

    // =====================================================
    // ASSESSMENT METHODS
    // =====================================================

    fun insertAssessment(
        skill: String,
        score: Int,
        total: Int,
        percentage: Int
    ): Long {

        val db =
            writableDatabase

        val values =
            ContentValues()

        values.put(
            COL_ASSESSMENT_SKILL,
            skill
        )

        values.put(
            COL_ASSESSMENT_SCORE,
            score
        )

        values.put(
            COL_ASSESSMENT_TOTAL,
            total
        )

        values.put(
            COL_ASSESSMENT_PERCENTAGE,
            percentage
        )

        val result =
            db.insert(
                TABLE_ASSESSMENTS,
                null,
                values
            )

        db.close()

        return result
    }

    // =====================================================
    // LATEST ASSESSMENT
    // =====================================================

    fun getLatestAssessmentPercentage(): Int {

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

        var percentage = 0

        cursor.use {

            if (it.moveToFirst()) {

                percentage =
                    it.getInt(
                        it.getColumnIndexOrThrow(
                            COL_ASSESSMENT_PERCENTAGE
                        )
                    )
            }
        }

        db.close()

        return percentage
    }

    // =====================================================
    // ASSESSMENT FOR SPECIFIC SKILL
    // =====================================================

    fun getAssessmentPercentageForSkill(
        skillName: String
    ): Int {

        val db =
            readableDatabase

        val cursor =
            db.query(
                TABLE_ASSESSMENTS,
                arrayOf(
                    COL_ASSESSMENT_PERCENTAGE
                ),
                "LOWER($COL_ASSESSMENT_SKILL) = LOWER(?)",
                arrayOf(skillName),
                null,
                null,
                "$COL_ASSESSMENT_ID DESC",
                "1"
            )

        var percentage = 0

        cursor.use {

            if (it.moveToFirst()) {

                percentage =
                    it.getInt(
                        it.getColumnIndexOrThrow(
                            COL_ASSESSMENT_PERCENTAGE
                        )
                    )
            }
        }

        db.close()

        return percentage
    }

    // =====================================================
    // AVERAGE SKILL PROGRESS
    // =====================================================

    fun getAverageSkillProgress(): Int {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT AVG($COL_SKILL_PROGRESS)
                FROM $TABLE_SKILLS
                """.trimIndent(),
                null
            )

        var average = 0

        cursor.use {

            if (it.moveToFirst() &&
                !it.isNull(0)
            ) {

                average =
                    it.getDouble(0)
                        .toInt()
            }
        }

        db.close()

        return average
    }
}