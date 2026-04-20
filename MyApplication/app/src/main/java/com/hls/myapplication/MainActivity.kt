package com.hls.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hls.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val todoViewModel by viewModels<TodoViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainScope = MainScope()
        setContent {
            MyApplicationTheme {
                demo123(this.applicationContext)
            }

        }
    }


//    @Composable
//    fun Card(msg: Message) {
//        Row(
//            modifier = Modifier
//                .padding(10.dp)
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            Image(
//                painter = painterResource(R.drawable.a3),
//                contentDescription = null,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(40.dp)
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            var flag by remember { mutableStateOf(false) }
//            val myColor by animateColorAsState(
//                if (flag) {
//                    MaterialTheme.colorScheme.surface
//                } else {
//                    MaterialTheme.colorScheme.background
//                }
//            )
//            Column(Modifier.clickable { flag = !flag }) {
//                Text(
//                    msg.name,
//                    color = MaterialTheme.colorScheme.tertiary
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Surface(
//                    color = myColor, shape = MaterialTheme.shapes.medium,
//                    shadowElevation = 1.dp,
//                    modifier = Modifier
//                        .animateContentSize()
//                        .padding(1.dp)
//                ) {
//                    Text(
//                        msg.info,
//                        style = MaterialTheme.typography.bodyMedium,
//                        maxLines = if (flag) Int.MAX_VALUE else 1,
//                        modifier = Modifier.padding(4.dp)
//                    )
//                }
//
//            }
//        }
//
//
//    }
//
//    @Composable
//    fun Page() {
//        val list = mutableListOf<Message>()
//        for (i in 1..20) {
//            list += Message(
//                name = "hls$i", info = "dhjashjndashjndhjnasdjnasdjhasdjnasjhndasjnd" +
//                        "sdhjahshdbhjasdbhjashjdbas" +
//                        "ahsjdbjabhjdashjbdasbhjdjhbashjdbas" +
//                        "jhnasdnjkasjnkdasjnkdasjnkdkjnasdjnkasjnkdasjnkdas"
//            )
//        }
//
//
//        LazyColumn(
//            Modifier
//                .padding(top = 30.dp)
//
//        ) {
//            items(list) {
//                Card(it)
//            }
//        }
//
//    }
}