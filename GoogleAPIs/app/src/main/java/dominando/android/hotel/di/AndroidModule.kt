package dominando.android.hotel.di

import android.content.Context
import com.google.gson.GsonBuilder
import dominando.android.hotel.hotel_details.HotelDetailsViewModel
import dominando.android.hotel.hotel_form.HotelFormViewModel
import dominando.android.hotel.hotel_list.HotelListViewModel
import dominando.android.hotel.auth.Auth
import dominando.android.hotel.auth.AuthManager
import dominando.android.hotel.fcm.TokenManager
import dominando.android.hotel.repository.HotelRepository
import dominando.android.hotel.repository.http.HotelHttp
import dominando.android.hotel.repository.http.HotelHttpApi
import dominando.android.hotel.repository.imagefiles.FindHotelPicture
import dominando.android.hotel.repository.imagefiles.ImageGalleryPictureFinder
import dominando.android.hotel.repository.room.HotelDataBase
import dominando.android.hotel.repository.room.RoomRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    factory {
        val context = get() as Context
        val resolver = context.contentResolver
        val uploadDir = context.externalCacheDir ?: context.cacheDir
        ImageGalleryPictureFinder(uploadDir, resolver) as FindHotelPicture
    }

    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(HotelHttp.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()

        retrofit.create<HotelHttpApi>(HotelHttpApi::class.java)
    }

    factory {
        HotelHttp(service = get(), repository = get(), pictureFinder = get(), auth = get())
    }

    single {
        val manager: AuthManager = get()
        manager as Auth
    }

    single {
        AuthManager(context = get())
    }

    single {
        TokenManager(context = get(), hotelHttp = get())
    }
}