package dominando.android.http.model

import com.google.gson.annotations.SerializedName
import dominando.android.http.model.Book

data class Category(
    @SerializedName("categoria") var name: String = "",
    @SerializedName("livros") var books: List<Book> = emptyList()
)