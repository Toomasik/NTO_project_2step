package ru.myitschool.work.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.core.Constants.USE_TEST
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.CheckAndSaveAuthCodeUseCase

class AuthViewModel : ViewModel() {
    private val checkAndSaveAuthCodeUseCase by lazy { CheckAndSaveAuthCodeUseCase(AuthRepository) }
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Data()) // mutable - изменяемый соотв можно изменять сотояние элементов
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow() // тока читать поток типа get(сотояние элементов)

    private val _actionFlow: MutableSharedFlow<Unit> = MutableSharedFlow() // mutable - изменяемый соотв можно добавлять что то в поток
    val actionFlow: SharedFlow<Unit> = _actionFlow // тока читать поток типа get(данные с потока)

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> {
                viewModelScope.launch(Dispatchers.Default) {
                    _uiState.update { AuthState.Loading }
                    checkAndSaveAuthCodeUseCase.invoke(intent.text).fold(
                        onSuccess = {
                            _actionFlow.emit(Unit)
                        },
                        onFailure = { error ->
                            error.printStackTrace()
                            if(USE_TEST) _actionFlow.emit(Unit) // test переход на main передаем void обьект в поток это ловит(собирает) корутина с помощью collect
                            _uiState.value = AuthState.Data(
                                err = error.message ?: "Неизвестная ошибка"
                            )
                        }
                    )
                }
            }
            // is AuthIntent.TextInput -> Unit
            is AuthIntent.ResetError -> {
                val currentState = _uiState.value
                if (currentState is AuthState.Data && currentState.err.isNotEmpty())  {
                    _uiState.value = AuthState.Data("")
                }
            }
        }
    }
}