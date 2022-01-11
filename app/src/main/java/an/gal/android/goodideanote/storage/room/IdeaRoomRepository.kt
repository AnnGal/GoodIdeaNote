package an.gal.android.goodideanote.storage.room

import an.gal.android.goodideanote.data.Idea
import an.gal.android.goodideanote.storage.IdeaMapper

interface IdeaRoomRepository {
    suspend fun getAllIdeas(): List<Idea>
    suspend fun writeIdea(idea: Idea)
    suspend fun updateIdea(id: Long, title: String, text: String)
    suspend fun getIdeaById(id: Long): Idea?
    suspend fun deleteIdea(id: Long)
}

class IdeaRoomRepositoryImpl : IdeaRoomRepository {
    private val ideaDB = IdeaDatabase.instance

    override suspend fun getAllIdeas(): List<Idea> {
        return ideaDB.ideaDao().getAllIdeas().map { IdeaMapper.toIdeaPresenter(it) }
    }

    override suspend fun writeIdea(idea: Idea) {
        ideaDB.ideaDao().insert(IdeaMapper.toIdeaEntity(idea))
    }

    override suspend fun updateIdea(id: Long, title: String, text: String) {
        ideaDB.ideaDao().update(id, title, text)
    }

    override suspend fun getIdeaById(id: Long): Idea {
        return IdeaMapper.toIdeaPresenter(ideaDB.ideaDao().getIdeaById(id))
    }

    override suspend fun deleteIdea(id: Long) {
        ideaDB.ideaDao().delete(id)
    }
}
