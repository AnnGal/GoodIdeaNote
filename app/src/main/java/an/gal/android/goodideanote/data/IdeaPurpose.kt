package an.gal.android.goodideanote.data

import an.gal.android.goodideanote.R

enum class IdeaPurpose(val id: Int, val text: String, val image: Int) {
    Skills(1, "Skills", R.drawable.ic_skills),
    Home(2, "Home", R.drawable.ic_home),
    Work(3, "Work", R.drawable.ic_work);

    companion object {

        fun getPurposeList(): List<String> = values().map { ideaPurpose: IdeaPurpose ->  ideaPurpose.text }

        /** get id by name or id of first item */
        fun getIdByName(text: String): Int {
            return values().filter { it.text.equals(text, ignoreCase = true) }.maxOrNull()?.id ?: getDefaultPurpose()
        }

        /** get image by id or image of first item */
        fun getImageById(id: Int): Int {
            return values().filter { it.id == id }.maxOrNull()?.image ?: getDefaultPurposeImage()
        }

        fun getDefaultPurpose(): Int = values()[0].id

        private fun getDefaultPurposeImage(): Int = values()[0].image
    }
}


