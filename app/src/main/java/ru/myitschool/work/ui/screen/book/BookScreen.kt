package ru.myitschool.work.ui.screen.book

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.R
import ru.myitschool.work.core.Constants.USE_TEST
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.ui.nav.MainScreenDestination

@Composable
fun BookScreen(
    navController: NavController,
    vm: BookViewModel = viewModel(),
    onBack: () -> Unit = { navController.popBackStack() }
) {
    val state by vm.state.collectAsState()
    val selectedRoomId by vm.selectedRoomId.collectAsState()
    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            if(USE_TEST) Log.d("event!", "event: $ev")
            when (ev) {
                is BookViewModel.BookEvent.BookingSuccess -> {
                    //обновить данные на main
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("need_reload", true)
                    // возврат
                    navController.popBackStack()
                }

                is BookViewModel.BookEvent.BookingError -> {}
            }
        }
    }
    when (state) {
        is BookState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BookState.Data -> {
            Column(Modifier.padding(5.dp)) {
                val dates = (state as BookState.Data).bookings.keys.toList()
                val rooms = (state as BookState.Data).rooms
                Row() {
                    IconButton({ onBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.back_btn),
                            contentDescription = "back",
                            modifier = Modifier
                                .testTag(TestIds.Book.BACK_BUTTON)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if ((state as BookState.Data).err.isNotEmpty()) {
                        IconButton(
                            onClick = { vm.retry() },
                            Modifier.padding(5.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.refresh_btn),
                                contentDescription = "refresh",
                                modifier = Modifier
                                    .testTag(TestIds.Book.REFRESH_BUTTON)
                                    .padding(7.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.padding(horizontal = 25.dp))
                if((state as BookState.Data).err.isEmpty() && dates.isEmpty()) { // на даты по условию, на комнаты не было
                    Text("Все забронированно", Modifier.fillMaxWidth().padding(30.dp).testTag(TestIds.Book.EMPTY),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center)
                }
                if((state as BookState.Data).err.isEmpty() && dates.isNotEmpty()) {
                    LazyRow() {
                        itemsIndexed(dates) { index, date ->
                            val selected = date == (state as BookState.Data).date
                            val stroke = if (selected) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else BorderStroke(1.dp, Color.Gray)
                            OutlinedButton(
                                { vm.selectedDate(date) },
                                Modifier.padding(6.dp)
                                    .testTag(TestIds.Book.getIdDateItemByPosition(index)),
                                border = stroke,
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    date, Modifier.testTag(TestIds.Book.ITEM_DATE),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Spacer(Modifier.padding(horizontal = 5.dp))
                    LazyColumn() {
                        itemsIndexed(rooms) { index, room ->
                            val isSelected = selectedRoomId == room.id
                            Card(
                                Modifier.fillMaxWidth().padding(6.dp).border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                    .selectable(
                                        selected = isSelected,
                                        onClick = { vm.selectRoom(room.id) })
                                    .testTag(TestIds.Book.getIdPlaceItemByPosition(index)),
                                RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(3.dp)
                            ) {
                                Row() {
                                    Text(
                                        room.place,
                                        Modifier.padding(5.dp).testTag(TestIds.Book.ITEM_PLACE_TEXT)
                                    )
                                    Spacer(Modifier.weight(1f))
                                    RadioButton(
                                        selected = isSelected,
                                        null,
                                        Modifier.padding(5.dp)
                                            .testTag(TestIds.Book.ITEM_PLACE_SELECTOR)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    if (rooms.isNotEmpty()) {
                        Button(
                            {
                                vm.addBook(selectedRoomId, (state as BookState.Data).date)
                                if(USE_TEST) Log.d("On click!", "Click addBook selectedRoomId=$selectedRoomId date=${(state as? BookState.Data)?.date}")

                            },
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .testTag(TestIds.Book.BOOK_BUTTON),
                            shape = RoundedCornerShape(10.dp),

                            ) {
                            Text("Забронировать")
                            RoundedCornerShape(3.dp)
                        }
                    }
                }
                if ((state as BookState.Data).err.isNotEmpty()) {
                    Box(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .padding(horizontal = 15.dp),
                        Alignment.Center
                    ) {
                        Box(Modifier.clip(RoundedCornerShape(5.dp)).background(Color.DarkGray).padding(horizontal = 5.dp, vertical = 5.dp)) {
                            Text(
                                (state as BookState.Data).err,
                                Modifier.testTag(TestIds.Book.ERROR),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                )
                        }
                    }
                }
            }
        }
    }
}