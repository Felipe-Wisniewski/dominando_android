package dominando.android.fragments.ui.HotelList

import dominando.android.fragments.model.Hotel
import dominando.android.fragments.model.HotelRepository

class HotelListPresenter(private val view: HotelListView, private val repository: HotelRepository) {

    fun searchHotels(term: String) {
        repository.search(term) { hotels ->
            view.showHotels(hotels)
        }
    }

    fun showHotelDetails(hotel: Hotel) {
        view.showHotelDetails(hotel)
    }
}