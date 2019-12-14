package dominando.android.hotel.di

import dominando.android.hotel.HotelDetails.HotelDetailsViewModel
import dominando.android.hotel.HotelForm.HotelFormViewModel
import dominando.android.hotel.HotelList.HotelListViewModel
import dominando.android.hotel.repository.HotelRepository
import dominando.android.hotel.repository.room.HotelDataBase
import dominando.android.hotel.repository.room.RoomRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { this }

    single {
        RoomRepository(HotelDataBase.getDataBase(context = get())) as HotelRepository
    }

    viewModel {
        HotelListViewModel(repository = get())
    }

    viewModel {
        HotelDetailsViewModel(repository = get())
    }

    viewModel {
        HotelFormViewModel(repository = get())
    }
}