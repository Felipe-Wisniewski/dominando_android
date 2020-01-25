package dominando.android.http.model

import com.google.gson.annotations.SerializedName
import dominando.android.http.model.Category

data class Publisher(
    @SerializedName("novatec") var categories: List<Category> = emptyList()
)