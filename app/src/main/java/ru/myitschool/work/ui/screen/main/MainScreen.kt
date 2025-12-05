package ru.myitschool.work.ui.screen.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.ui.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Column(Modifier.fillMaxSize()) {

        /*Text(stringResource(
            R.string.main_welcome),
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp,top = 12.dp, bottom = 12.dp)
        )*/ /** было до меня хз зачем в условии ни слова но текстовое поле создали если время будет сделаю в виде всплывающего уведомления при входе */
        Row (Modifier.fillMaxWidth().background(Color.Gray),
            verticalAlignment = Alignment.CenterVertically){
            IconButton(
                onClick = {},
                Modifier.padding(5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.refresh_btn),
                    contentDescription = "refresh",
                    modifier = Modifier.testTag(TestIds.Main.REFRESH_BUTTON)
                        .padding(7.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton( // потом переделаю в обычную кнопку
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.logout_btn),
                    contentDescription = "refresh",
                    modifier = Modifier.testTag(TestIds.Main.LOGOUT_BUTTON)
                        .padding(0.dp)
                )
            }
        }

        Column(Modifier.fillMaxWidth().background(Color.Blue),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(4.dp))
            Image(
                painterResource(R.drawable.cot_so_shlapoi), // TODO: фото с сервака
                "ава",
                Modifier.clip(CircleShape).size(230.dp),
                Alignment.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Lorem ipsum",
                modifier = Modifier.testTag(TestIds.Main.PROFILE_NAME), // TODO: имя с сервака
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(25.dp))
            Button(
                {},
                Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gray,
                    contentColor = Color.White
                )

            ) {
                Text("Забронировать")
                Color.Gray
                RoundedCornerShape(3.dp)
                //Modifier.clip(RoundedCornerShape(3.dp)).background(Gray)
            }
        }
        /** вот сюдо нужно list c датой и комнатой) */
        /*LazyColumn(Modifier.padding(horizontal = 15.dp)) {
            items(bookingList)
        }*/
    }
}