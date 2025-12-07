package ru.myitschool.work.ui.screen.main

import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.myitschool.work.core.Constants.USE_TEST
import ru.myitschool.work.data.dto.Booking
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }
    private fun loadProfile() {
        viewModelScope.launch {

            // todo: test data
            if(USE_TEST) {
                _state.value = ProfileState.Success(
                    "Мистер кот",
                    "https://upload.wikimedia.org/wikipedia/ru/b/b9/Кошка_Вирго.jpg",
                    formDate(mapOf(
                        "2025-02-02" to Booking(1,"комната 1"),
                        "2025-01-15" to Booking(id = 1, place = "102"),
                        "2025-02-05" to Booking(id = 2, place = "209.13"),
                        "2025-01-09" to Booking(id = 3, place = "Зона 51. 50"),
                        "2025-01-11" to Booking(id = 1, place = "102"),
                        "2025-01-12" to Booking(id = 2, place = "205.13"),
                        "2025-01-13" to Booking(id = 3, place = "Зtна 51. 50"),
                        "2025-01-14" to Booking(id = 1, place = "102"),
                        "2025-01-15" to Booking(id = 2, place = "29.13"),
                        "2025-01-08" to Booking(id = 3, place = "З6на 51. 50"),
                        "2025-01-21" to Booking(id = 23, place = "102"),
                        "2025-01-25" to Booking(id = 55, place = "209.13"),
                        "2025-01-23" to Booking(id = 3, place = "Зона4f 51. 50")
                    ))
                )
                return@launch
            }

            _state.value = ProfileState.Loading

            val code = AuthRepository.codeCache ?: run {
                _state.value = ProfileState.Error("Код потерялся")
                return@launch
            }

            val result = UserRepository.getUserInfo(code)

            if (result.isSuccess) {
                val userInfo = result.getOrNull()!!
                _state.value = ProfileState.Success(
                    name = userInfo.name,
                    photo = userInfo.photoUrl,
                    bookings = formDate(userInfo.booking)
                )
            } else {
                _state.value = ProfileState.Error("Не получилось загрузить данные")
            }
        }
    }
    //
    fun retry() {
        loadProfile()
    }
    fun logout() {
        AuthRepository.codeCache = null
        _state.value = ProfileState.Loading
    }
    fun safe() {}
    private fun formDate(bookings: Map<String, Booking>): Map<String, Booking> {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val output = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return bookings.entries
            .map { entry ->
                val date = LocalDate.parse(entry.key, input)
                date to entry.value
            }.sortedBy { it.first }
            .associate { (date, booking) ->
                date.format(output) to booking
            }

    }
}