package an.gal.android.goodideanote.storage.cursor

data class IdeaCursorEntity (
        var id: Long? = -1,
        var title: String = "",
        var text: String? = "",
        var purposeId: Int = 1
)
