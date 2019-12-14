package dominando.android.hotel.repository

import androidx.lifecycle.LiveData
import dominando.android.hotel.model.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun remove(vararg hotels: Hotel)
//    fun hotelById(id: Long, callback: (Hotel?) -> Unit)
//    fun search(term: String, callback: (List<Hotel>) -> Unit)
    fun hotelById(id: Long): LiveData<Hotel>
    fun search(term: String): LiveData<List<Hotel>>
}