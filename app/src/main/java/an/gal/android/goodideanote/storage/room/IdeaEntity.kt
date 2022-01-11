package an.gal.android.goodideanote.storage.room

import an.gal.android.goodideanote.storage.DbContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbContract.IdeaContract.TABLE_NAME)
data class IdeaEntity (
    @PrimaryKey
    @ColumnInfo(name = DbContract.IdeaContract.COLUMN_NAME_ID)
    val id: Long?,
    val title: String,
    val text: String?,
    val purposeId: Int
)

