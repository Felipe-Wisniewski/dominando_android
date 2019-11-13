package dominando.android.fragments.ui.HotelDetails

import dominando.android.fragments.model.Hotel

interface HotelDetailsView {
    fun showHotelsDetails(hotel: Hotel)
    fun errorHotelNotFound()
}