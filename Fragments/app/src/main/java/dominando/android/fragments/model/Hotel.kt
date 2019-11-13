package dominando.android.fragments.model

data class Hotel(
    var id: Long = 0,
    var name: String = "",
    var addres: String = "",
    var rating: Float = 0.0F
) {
    override fun toString(): String = name
}