package dominando.android.hotel.HotelDetails

import dominando.android.hotel.model.Hotel

interface HotelDetailsView {
    fun showHotelsDetails(hotel: Hotel)
    fun errorHotelNotFound()
}