package dominando.android.fragments.ui.HotelList

import dominando.android.fragments.model.Hotel

interface HotelListView {
    fun showHotels(hotels: List<Hotel>)
    fun showHotelDetails(hotel: Hotel)
}