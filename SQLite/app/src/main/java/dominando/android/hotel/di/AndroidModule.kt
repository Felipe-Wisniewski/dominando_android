package dominando.android.hotel.di

import dominando.android.hotel.HotelDetails.HotelDetailsPresenter
import dominando.android.hotel.HotelDetails.HotelDetailsView
import dominando.android.hotel.HotelForm.HotelFormPresenter
import dominando.android.hotel.HotelForm.HotelFormView
import dominando.android.hotel.HotelList.HotelListPresenter
import dominando.android.hotel.HotelList.HotelListView
import dominando.android.hotel.repository.HotelRepository
import dominando.android.hotel.repository.sqlite.SQLiteRepository
import org.koin.dsl.module

val androidModule = module {
    single { this }

    single {
        SQLiteRepository(ctx = get()) as HotelRepository
    }

    factory { (view: HotelListView) ->
        HotelListPresenter(view, repository = get())
    }

    factory { (view: HotelDetailsView) ->
        HotelDetailsPresenter(view, repository = get())
    }

    factory { (view: HotelFormView) ->
        HotelFormPresenter(view, repository = get())
    }
}