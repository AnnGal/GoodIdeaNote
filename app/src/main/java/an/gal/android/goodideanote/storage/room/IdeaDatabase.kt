package an.gal.android.goodideanote.storage.room

import an.gal.android.goodideanote.App
import an.gal.android.goodideanote.storage.DbContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IdeaEntity::class], version = 1, exportSchema = false)
abstract class IdeaDatabase : RoomDatabase() {

    abstract fun ideaDao(): IdeaDao

    companion object {
        val instance: IdeaDatabase by lazy {
            Room.databaseBuilder(
                App.context(),
                IdeaDatabase::class.java,
                DbContract.DATABASE_NAME
            ).build()
        }
    }
}
