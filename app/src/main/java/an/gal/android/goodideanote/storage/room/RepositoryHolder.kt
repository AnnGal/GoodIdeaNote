package an.gal.android.goodideanote.storage.room

object RepositoryHolder {

    private val ideaRoomRepository by lazy { IdeaRoomRepositoryImpl() }
    fun ideaRoomRepository(): IdeaRoomRepositoryImpl = ideaRoomRepository

}