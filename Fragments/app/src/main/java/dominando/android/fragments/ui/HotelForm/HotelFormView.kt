package dominando.android.fragments.ui.HotelForm

import dominando.android.fragments.model.Hotel

interface HotelFormView {
    fun showHotel(hotel: Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}