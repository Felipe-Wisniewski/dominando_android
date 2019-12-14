package dominando.android.hotel.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.sqlite.DATABASE_NAME
import dominando.android.hotel.repository.sqlite.DATABASE_VERSION

@Database(entities = [Hotel::class], version = DATABASE_VERSION)
abstract class HotelDataBase : RoomDatabase() {

    companion object {
        private var instance: HotelDataBase? = null

        fun getDataBase(context: Context): HotelDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    HotelDataBase::class.java,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as HotelDataBase
        }

        fun destroyInstance() {
            instance = null
        }
    }

    abstract fun hotelDao(): HotelDao
}