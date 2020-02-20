package dominando.android.enghaw.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return value.split(',')
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        val sb = StringBuffer()

        list.forEachIndexed { index, s ->
            if (index > 0) sb.append(',')
            sb.append(s)
        }

        return sb.toString()
    }
}