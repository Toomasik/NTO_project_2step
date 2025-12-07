package ru.myitschool.work.ui.screen.book

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.myitschool.work.core.Constants.USE_TEST
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookingRepository
import ru.myitschool.work.data.repo.CreateBookRepository
import ru.myitschool.work.data.repo.UserRepository
import ru.myitschool.work.ui.screen.main.ProfileState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BookViewModel: ViewModel() {
    private val _state = MutableStateFlow<BookState>(BookState.Loading)
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<BookEvent>(replay = 0)
    val events = _events.asSharedFlow()

    init { load() }
    private fun load() {
        viewModelScope.launch {
            //_state.value = BookState.Data()
            val code = AuthRepository.codeCache ?: run {
                _state.value = BookState.Data(err = "")
                return@launch
            }
            val result = BookingRepository.getBooking(code)
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val output = DateTimeFormatter.ofPattern("dd.MM")
            if (result.isSuccess) {
                val booking = result.getOrNull()!!.booking.filter { it.value.isNotEmpty() }
                    .mapKeys { (key,_) ->
                    LocalDate.parse(key, input).format(output)
                }
                val date = booking.keys.minOfOrNull { LocalDate.parse(it, output) }?.format(output)
                _state.value = BookState.Data(booking,date)
            } else {
                _state.value = BookState.Data(err = "Не получилось загрузить данные")
            }
            return@launch
        }
    }
    fun retry() { load() }
    private val _selectedRoomId = MutableStateFlow<Int?>(null)
    val selectedRoomId = _selectedRoomId.asStateFlow()
    fun selectRoom(roomId: Int) {_selectedRoomId.value = roomId}
    fun selectedDate(date: String) {
        val current = _state.value as? BookState.Data ?: return
        _state.value = current.copy(date = date)
        _selectedRoomId.value = null
    }
    fun addBook(id: Int?, date: String?) {
        if(USE_TEST) Log.d("I'm in book!", "addBook started for id=$id date=$date")
        val code = if(USE_TEST) {"test"} else {AuthRepository.codeCache ?: run {
            _state.value = BookState.Data(err = "Код потерялся")
            return
        }
        }
        if (id == null || date == null) {
            //_state.value = BookState.Data(err = "Выберите комнату") // ну не стоит могут посчитать за ошибку т.к. нужно обновлять
            return
        }
        _state.value = BookState.Loading
        viewModelScope.launch {
            if (USE_TEST) {
                delay(5000)
                Log.d("I'm in vmScope!", "emitting BookingSuccess")
                _events.emit(BookEvent.BookingSuccess)
            } else {
                try {
                    CreateBookRepository.CreateBook(code, date, id).onSuccess {
                        _events.emit(BookEvent.BookingSuccess)
                    }.onFailure {
                        val current = _state.value as? BookState.Data
                        _state.value = current?.copy(err = "Не удалось забронировать")
                            ?: BookState.Data(err = "Не удалось забронировать")
                        _events.emit(BookEvent.BookingError)
                    }
                } catch (e: Exception) {
                    _state.value = BookState.Data(err = e.toString())
                    _events.emit(BookEvent.BookingError)
                }
            }
        }
    }
    sealed class BookEvent {
        object BookingSuccess : BookEvent()
        object BookingError : BookEvent()
    }
}