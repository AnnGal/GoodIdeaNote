package an.gal.android.goodideanote.storage

import android.provider.BaseColumns

object DbContract {

    const val DATABASE_NAME = "Idea_db"

    object IdeaContract {
        const val TABLE_NAME = "idea"
        const val COLUMN_NAME_ID = BaseColumns._ID
    }
}