package dominando.android.hotel.repository.memory

import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.HotelRepository

object MemoryRepository : HotelRepository {

    private var nextId = 1L
    private val hotelsList = mutableListOf<Hotel>()

    init {
        save(
            Hotel(
                0,
                "New Beach Hotel",
                "Av. Boa Viagem",
                4.5F
            )
        )
        save(
            Hotel(
                0,
                "Old Beach Hotel",
                "Av. Viagem Boa",
                5F
            )
        )
        save(
            Hotel(
                0,
                "New City Hotel",
                "Av. Daora Viagem",
                4.1F
            )
        )
        save(
            Hotel(
                0,
                "Old City Hotel",
                "Av. Irada Viagem",
                4F
            )
        )
        save(
            Hotel(
                0,
                "New Island Hotel",
                "Av. Top Viagem",
                3.5F
            )
        )
        save(
            Hotel(
                0,
                "Old Island Hotel",
                "Av. Viagem Animal",
                3F
            )
        )
        save(
            Hotel(
                0,
                "New Boat Hotel",
                "Av. Viagem",
                1.5F
            )
        )
        save(
            Hotel(
                0,
                "Old Boat Hotel",
                "Av. Ótima Viagem",
                1.5F
            )
        )
        save(
            Hotel(
                0,
                "New Bikini Hotel",
                "Av. Legal Viagem",
                0.5F
            )
        )
        save(
            Hotel(
                0,
                "Old Bikini Hotel",
                "Av. Bacana Viagem",
                1F
            )
        )
        save(
            Hotel(
                0,
                "New Hotel",
                "Av. Mal Viagem",
                4.1F
            )
        )
    }

    override fun save(hotel: Hotel) {
        if (hotel.id == 0L) {
            hotel.id = nextId++
            hotelsList.add(hotel)

        } else {
            val index = hotelsList.indexOfFirst { it.id == hotel.id }

            if (index > -1) {
                hotelsList[index] = hotel
            } else {
                hotelsList.add(hotel)
            }
        }
    }

    override fun remove(vararg hotels: Hotel) {
        hotelsList.removeAll(hotels)
    }

    override fun hotelById(id: Long, callback: (Hotel?) -> Unit) {
        callback(hotelsList.find { it.id == id })
    }

    override fun search(term: String, callback: (List<Hotel>) -> Unit) {
        val resultList = if (term.isEmpty()) hotelsList
            else hotelsList.filter { it.name.toUpperCase().contains(term.toUpperCase()) }

        callback(resultList.sortedBy { it.name })
    }
}