package an.gal.android.goodideanote.storage.cursor

import an.gal.android.goodideanote.data.Idea
import an.gal.android.goodideanote.storage.DbContract
import an.gal.android.goodideanote.storage.room.IdeaEntity
import an.gal.android.goodideanote.storage.IdeaMapper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.sqlite.transaction

//private const val LOG_TAG = "SQLiteOpenHelper"
private const val DATABASE_NAME = DbContract.DATABASE_NAME
private const val TABLE_NAME = DbContract.IdeaContract.TABLE_NAME
private const val DATABASE_VERSION = 1
private const val CREATE_TABLE_SQL =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id LONG PRIMARY KEY AUTOINCREMENT, title VARCHAR(500), title VARCHAR(2500), purposeId INTEGER);"

class CursorDatabaseSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(CREATE_TABLE_SQL)
        } catch (exception: SQLException) {
            //Log.e(LOG_TAG, "Exception while trying to create database", exception)
        }
        //Log.d(LOG_TAG, "onCreate called")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Log.d(LOG_TAG, "onUpgrade called")
    }

    fun getIdeasFromCursor(): List<Idea> {
        val listOfIdeas = mutableListOf<IdeaEntity>()
        getCursorWithIdeas().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    listOfIdeas.add(getDataFromCursor(cursor))
                } while (cursor.moveToNext())
            }
        }
        return listOfIdeas.map { IdeaMapper.toIdeaPresenter(it) }
    }

    /** get all rows */
    private fun getDataFromCursor(cursor: Cursor): IdeaEntity {
        var col = 0
        val idea = IdeaCursorEntity().apply {
            id = cursor.getLong(col++)
            title = cursor.getString(col++)
            text = cursor.getString(col++)
            purposeId = cursor.getInt(col++)
        }

        return IdeaEntity(idea.id, idea.title, idea.text, idea.purposeId)
    }

    private fun getCursorWithIdeas(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    /** get row by id */
    fun getCursorIdeaById(id: Long): Idea? {
        val ideasCursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE " + BaseColumns._ID + " = ?",  arrayOf(id.toString()))

        var idea: IdeaEntity? = null
        try {
            ideasCursor.moveToFirst()
            idea = getDataFromCursor(ideasCursor)
        } catch (e: Exception) {
            //
        }

        return if (idea != null) { IdeaMapper.toIdeaPresenter(idea) } else null
    }

    /** insert */
    fun insertIdeaViaCursor(title: String, text: String, purposeId: Int) {
        val value = ContentValues().apply {
            put("title", title)
            put("text", text)
            put("purposeId", purposeId)
        }
        val db = writableDatabase
        db.transaction {
            insert(TABLE_NAME, null, value)
        }
    }

    /** update */
    fun updateIdeaViaCursor(id: Long, title: String, text: String, purposeId: Int) {
        val value = ContentValues().apply {
            put("title", title)
            put("text", text)
            put("purposeId", purposeId)
        }
        writableDatabase.update(TABLE_NAME,
            value,
            BaseColumns._ID + " = ?", arrayOf(id.toString()))
    }

    /** delete */
    fun deleteIdeaViaCursor(id: Long) {
        writableDatabase.delete(TABLE_NAME,
            BaseColumns._ID + " = ?", arrayOf(id.toString()))
    }

}

