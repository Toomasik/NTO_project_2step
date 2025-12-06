package ru.myitschool.work.ui.screen.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel: ViewModel() {
    private val _state = MutableStateFlow<BookState>(BookState.Loading)
    val state = _state.asStateFlow()

    init { load() }
    private fun load() {
        viewModelScope.launch {
            _state.value = BookState.Data()
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
        // отправить запрос в бд что бы добавили к текущему пользователю бронь(id_комнаты, дата бронирования)
    }
}