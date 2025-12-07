package ru.myitschool.work.ui.screen.book

import android.R
import ru.myitschool.work.data.dto.Booking


sealed class BookState {
    object Loading: BookState()
    data class Data(
        val bookings: Map<String, List<Booking>> = mapOf(
            "01.01" to listOf(
                Booking(1, "Переговорка 1"),
                Booking(2, "Холл 2")
            ),
            "02.01" to listOf(
                Booking(3, "Зона 3")
            ),
            "03.01" to listOf(

            ),
            "04.01" to listOf(
                Booking(4, "Комната 4"),
                Booking(5, "Комната B"),
                Booking(6, "Зал ж5")
            )
        ),
        val date: String? = bookings.keys.minOrNull(),
        val err: String = ""
    ): BookState() {
        val rooms: List<Booking>
            get() = date?.let { bookings[it] } ?: emptyList()
    }

    /**
    По умолчанию выбирается самая ранняя доступная дата (например, из набора "5 января", "6 января", "9 января" будет показана дата "5 января").
     */ // я таких дат не видел в примере кста ток с цифрами
}