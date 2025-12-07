package ru.myitschool.work.ui.screen.main


import android.icu.util.LocaleData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination
import ru.myitschool.work.ui.theme.Gray
import ru.myitschool.work.ui.theme.IntenceGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//@OptIn(ExperimentalMaterial3Api::class) // <- это для TopAppBar надо было т.к. она в эксперементальном режиме
@Composable
fun MainScreen(
    navController: NavController,
    vm: ProfileViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    when (state) {
        is ProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileState.Error -> {
            Column(Modifier.fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { vm.retry() },
                    Modifier.padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.refresh_btn),
                        contentDescription = "refresh",
                        modifier = Modifier
                            .testTag(TestIds.Main.REFRESH_BUTTON)
                    )
                }
                Text((state as ProfileState.Error).message,Modifier.testTag(TestIds.Main.ERROR))
            }
        }

        is ProfileState.Success -> {
            Column(Modifier.fillMaxSize()) {

                /*Text(stringResource(
                    R.string.main_welcome),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp,top = 12.dp, bottom = 12.dp)
                )*/
                /** было до меня хз зачем в условии ни слова но текстовое поле создали если можно будет сделаю в виде всплывающего уведомления при входе */
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {vm.retry()},
                        Modifier.padding(5.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.refresh_btn),
                            contentDescription = "refresh",
                            modifier = Modifier
                                .testTag(TestIds.Main.REFRESH_BUTTON)
                                .padding(7.dp)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton( // потом переделаю в обычную кнопку //todo: если drawable ресурс нельзя изменять то надо на обчную менять если можно то оставим
                        onClick = {
                            vm.logout()
                            navController.navigate(AuthScreenDestination) {
                                popUpTo(MainScreenDestination) {inclusive = true}
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.logout_btn),
                            contentDescription = "logout",
                            modifier = Modifier
                                .testTag(TestIds.Main.LOGOUT_BUTTON)
                                .padding(0.dp)
                        )
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(4.dp))
                    AsyncImage(
                        model = (state as ProfileState.Success).photo,
                        contentDescription = "ава",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(230.dp)
                            .testTag(TestIds.Main.PROFILE_IMAGE)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        (state as ProfileState.Success).name,
                        Modifier.testTag(TestIds.Main.PROFILE_NAME) ,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(25.dp))
                    Button(
                        {navController.navigate(BookScreenDestination)},
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                            .testTag(TestIds.Main.ADD_BUTTON),
                        shape = RoundedCornerShape(10.dp),
                        //colors = ButtonDefaults.buttonColors()

                    ) {
                        Text("Забронировать")
                        RoundedCornerShape(3.dp)
                        //Modifier.clip(RoundedCornerShape(3.dp)).background(Gray)
                    }
                }
                Spacer(Modifier.height(8.dp))
                val bookingList = (state as ProfileState.Success).bookings.toList()
                LazyColumn() {
                    itemsIndexed(bookingList) { index, item ->
                        Card(Modifier.fillMaxWidth().padding(6.dp).testTag(TestIds.Main.getIdItemByPosition(index)),
                            RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(3.dp)) {
                            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)).padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically ){
                                val label = item.second.place.take(1)
                                Box(
                                    Modifier.size(50.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                                    Alignment.Center
                                ) {
                                    Text(
                                        label,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 28.sp,
                                        //fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                Spacer(Modifier.padding(horizontal = 5.dp))
                                val place = item.second.place
                                Text(place,Modifier.testTag(TestIds.Main.ITEM_PLACE), fontWeight = FontWeight.ExtraBold)
                                Spacer(Modifier.weight(1f))
                                val date = item.first
                                Text(date, Modifier.testTag(TestIds.Main.ITEM_DATE), fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }
            }
        }
    }
}