package an.gal.android.goodideanote.storage.room

import androidx.room.*

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(idea: IdeaEntity)

    @Query("SELECT * FROM idea ORDER BY _id DESC")
    suspend fun getAllIdeas(): List<IdeaEntity>

    @Query("UPDATE idea SET title = :title, text = :text WHERE _id = :id")
    suspend fun update(id: Long, title: String, text: String): Int

    @Query("SELECT * FROM idea WHERE _id = :id")
    suspend fun getIdeaById(id: Long): IdeaEntity

    @Query("DELETE FROM idea WHERE _id = :id")
    suspend fun delete(id: Long)
}