package com.hls.myapplication


import android.R.attr.name
import android.R.attr.text
import android.annotation.SuppressLint
import android.app.Application
import android.companion.CompanionException
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.text.Layout
import android.util.Log
import android.view.Surface
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.unveilIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.DatabaseView
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.util.TableInfo
import androidx.room.util.copy
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.File
import java.nio.file.WatchEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.forEachIndexed
import kotlin.collections.listOf
import kotlin.math.absoluteValue
import kotlin.math.log
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun IntroCard() {


    Row(
        modifier = Modifier
            .padding(top = 30.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable() {}) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            modifier = Modifier.size(100.dp)

        ) {
            Image(
                painter = painterResource(R.drawable.a1), contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
            Text("hls", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "three minute age",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }

}


@Composable
fun Col() {

    val rememberScrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(rememberScrollState)) {
        repeat(100) {
            Text("当前是第$it 个元素")
        }
    }


}

@Composable
fun List() {
    val rememberLazyListState = rememberLazyListState()
    val rememberCoroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(onClick = {
                rememberCoroutineScope.launch {
                    rememberLazyListState.animateScrollToItem(0)
                }
            }, modifier = Modifier.weight(1f)) {
                Text("top")
            }
            Button(onClick = {
                rememberCoroutineScope.launch {
                    rememberLazyListState.animateScrollToItem(99)
                }
            }, modifier = Modifier.weight(1f)) {
                Text("end")
            }
        }
        LazyColumn(state = rememberLazyListState) {
            items(100) {
                Item(it)
            }
        }
    }

}


@Composable
fun Item(index: Int) {
    Row {
        Image(
            painterResource(R.drawable.a1), null, modifier = Modifier.size(50.dp)
        )
        Text("当前是第$index 个元素")
    }
}


@SuppressLint("SuspiciousModifierThen")
fun Modifier.firstBaseTop(h: Dp) = this.then(
    layout { measurable, constraints ->
        val measure = measurable.measure(constraints)
        val i = measure[FirstBaseline]
        val y = h.roundToPx() - i
        val height = measure.height + y
        layout(measure.width, height) {
            measure.placeRelative(0, y)
        }
    })

@Composable
fun Top() {
    Text(
        "hello world", modifier = Modifier
            .firstBaseTop(24.dp)
            .background(color = Color.Red)
    )

}


@Composable
fun MyCOl(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val result = measurables.map { it.measure(constraints) }
        var h = 0;
        layout(constraints.maxWidth, constraints.maxHeight) {
            result.forEach {
                it.placeRelative(0, h)
                h += it.height
            }
        }
    }

}


@Composable
fun MyCol() {

    MyCOl {
        Text("hls")
        Text("hls")
        Text("hls")
        Text("hls")
    }


}


@Composable
fun MyGrid(modifier: Modifier = Modifier, row: Int, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val map = measurables.map { it.measure(constraints) }

        val hRow = IntArray(row)
        val wRow = IntArray(row)

        map.forEachIndexed { index, placeable ->
            val i = index % row
            hRow[i] = max(hRow[i], placeable.height)
            wRow[i] += placeable.width
        }

        val height = hRow.sum()
        val width = wRow.max()
        layout(width, height) {
            val y = IntArray(row)
            for (i in 1 until row) {
                y[i] = y[i - 1] + hRow[i]
            }
            val x = IntArray(row)
            map.forEachIndexed { index, placeable ->
                val i = index % row
                placeable.placeRelative(x = x[i], y = y[i])
                x[i] += placeable.width
            }
        }

    }

}


@Composable
fun Ca(name: String) {
    Row(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp)


    ) {
        Image(
            painter = painterResource(R.drawable.a1),
            null,
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}


@Composable
fun MyDemo() {

    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        MyGrid(row = 3) {
            for (i in 1..30) {
                Ca("hello$i")
            }
        }
    }


}


@Composable
fun Btn() {
    ConstraintLayout {
        val (btn, text) = createRefs()
        Button(onClick = {}, modifier = Modifier.constrainAs(btn) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Text("click")
        }
        Text("hello", modifier = Modifier.constrainAs(text) {
            top.linkTo(btn.bottom, margin = 10.dp)
            centerHorizontallyTo(btn)
        })
    }
}


@Composable
fun Btn1() {
    ConstraintLayout {
        val (btn1, btn2, text) = createRefs()
        Button(onClick = {}, modifier = Modifier.constrainAs(btn1) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Text("click1")
        }

        Text("hello world11", modifier = Modifier.constrainAs(text) {
            top.linkTo(btn1.bottom, margin = 10.dp)
            centerAround(btn1.end)
        })
        val createEndBarrier = createEndBarrier(btn1, text)

        Button(onClick = {}, modifier = Modifier.constrainAs(btn2) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(createEndBarrier)
        }) {
            Text("click2")
        }
    }
}


@Composable
fun Btn2() {
    ConstraintLayout {
        val text = createRef()
        val createGuidelineFromStart = createGuidelineFromStart(0.5f)
        Text(
            "shadbhabhdashbdhbjasdhjbasdhbjasdbhjashjbdasbhjdasbhjdasbhj",
            modifier = Modifier.constrainAs(text) {
                linkTo(start = createGuidelineFromStart, end = parent.end)
                width = Dimension.preferredWrapContent
            })
    }
}


@Composable
fun Btn3() {

    BoxWithConstraints {
        val set = if (maxWidth > maxHeight) {
            Btnasd(100.dp)
        } else {
            Btnasd(20.dp)
        }

        ConstraintLayout(set) {
            Button(onClick = {}, modifier = Modifier.layoutId("btn")) {
                Text("click1")
            }

            Text("hello world11", modifier = Modifier.layoutId("text"))
        }
    }


}


@Composable
fun Stream() {

    var name = "hello"
    Column(Modifier.padding(top = 30.dp)) {

        Text(name)
        Spacer(modifier = Modifier.height(30.dp))
        TextField(name, onValueChange = {
            name = it
        })
    }

}


fun Btnasd(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val (btn, text) = createRefsFor("btn", "text")
        constrain(btn) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(btn.bottom, margin = margin)
        }
    }
}


@Composable
fun Btn4() {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text("hello", modifier = Modifier.weight(1f))
        VerticalDivider()
        Text("world", modifier = Modifier.weight(1f))
    }

}


class Page1ViewModel : ViewModel() {
    private var _list = MutableLiveData(listOf<String>())
    val list: LiveData<List<String>> = _list


    fun add(item: String) {
        _list.value = _list.value!! + listOf(item)
    }

    fun del(item: String) {
        _list.value = _list.value!!.toMutableList().apply {
            this -= item
        }
    }

}


@Composable
fun Page1(list: List<String>, add: (String) -> Unit, del: (String) -> Unit) {

    Column(modifier = Modifier.padding(top = 30.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(list) {
                Page1Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp), it, del)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = { add("hello world") }, modifier = Modifier.fillMaxWidth()) {
            Text("click")
        }
    }

}


@Composable
fun Page1Row(modifier: Modifier, info: String, del: (String) -> Unit) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                del(info)
            }) {
        Text(info)

        val remember = remember() { getAlpha() }
        Image(
            painter = painterResource(R.drawable.a1),
            null,
            alpha = LocalContentColor.current.copy(alpha = getAlpha()).alpha,
            modifier = modifier.size(30.dp)
        )
    }

}


@Composable
fun Page2Row(modifier: Modifier = Modifier, info: String, click: (String) -> Unit) {

    var text by remember { mutableStateOf("") }
    val current = LocalSoftwareKeyboardController.current

    Column {
        Row {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    click(text)
                    text = ""
                    current?.hide()
                })
            )
            Spacer(modifier.width(10.dp))
            Button(
                onClick = {
                    click(text)
                    text = ""
                }, enabled = text.isNotBlank()
            ) {
                Text("click", modifier.align(Alignment.CenterVertically))
            }
        }

        AnimatedVisibility(
            visible = text.isNotBlank(), enter = slideInVertically(
                initialOffsetY = { -it },  // 从上方进入
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),

            exit = slideOutVertically(
                targetOffsetY = { -it },   // 向上升起消失
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        ) {

            ImageList(modifier)

        }

    }

}

@Composable
fun ImageList(modifier: Modifier) {

    val list = listOf(painterResource(R.drawable.a1), painterResource(R.drawable.a3))
    var action by remember { mutableStateOf(list[0]) }

    Row {
        for (i in list) {
            ImageDemo(modifier, i, action) {
                action = it
            }
            Spacer(modifier.width(5.dp))
        }
    }
}

@Composable
fun ImageDemo(modifier: Modifier, painter: Painter, action: Painter, click: (Painter) -> Unit) {
    Column(modifier.clickable() { click(painter) }) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier.size(30.dp),
        )
        Spacer(modifier.height(4.dp))
        if (action == painter) {
            Box(
                modifier = modifier
                    .height(1.dp)
                    .width(30.dp)
                    .background(Color.Blue)
            )
        } else {
            Spacer(modifier.height(4.dp))
        }
    }


}

fun getAlpha(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}


//class TodoViewModel : ViewModel() {
//    private var _list = mutableStateListOf<Todo>()
//    private var isEdit = mutableStateOf(false)
//    private var currentItem=mutableStateOf(Todo())
//
//
//    val list=_list
//    val isEdit=_isEdit
//    val
//
//    fun changeItem(todo: Todo) {
//        currentItem.value=todo
//    }
//    fun changeItem(info: String) {
//        currentItem.value.info=info
//    }
//    fun changeItem(: Todo) {
//        currentItem.value=todo
//    }
//
//    fun toggle() {
//        isEdit.value=!isEdit.value
//    }
//
//    fun add(todo: Todo) {
//        list += todo
//    }
//
//    fun del(todo: Todo) {
//        list -= todo
//    }
//
//    fun change(index: Int, todo: Todo) {
//        list[index] = todo
//    }
//
//}
//
//
//@Composable
//fun TitleRow(todoViewModel: TodoViewModel) {
//    Column {
//        if (todoViewModel.isEdit.value) {
//            Text("Edit")
//            return
//        }
//        InputRow(
//            todoViewModel
//        )
//    }
//}
//
//@Composable
//fun InputRow(
//    todoViewModel: TodoViewModel
//) {
//    var info by remember { mutableStateOf(todoViewModel.currentItem.value.info) }
//    val current = LocalSoftwareKeyboardController.current
//    Column {
//        Row {
//            TextField(
//                value = info,
//                onValueChange = {
//                    info = it
//                    todo.info = it
//                    todoViewModel.currentItem.value.info="sh"
//                },
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions {
//                    info = ""
//                    current?.hide()
//                    add(todo)
//                },
//                modifier = Modifier.weight(1f)
//            )
//
//            if (!isEdit) {
//                Button(
//                    modifier = Modifier.padding(start = 5.dp),
//                    onClick = {
//                        current?.hide()
//                        info = ""
//                        add(todo)
//                    }
//                ) {
//                    Text("click")
//                }
//            }
//
//        }
//
//
//        var pic by remember { mutableIntStateOf(todo.image) }
//        Row {
//            for (id in Todo.images) {
//                Column(modifier = Modifier.padding(end = 5.dp)) {
//                    Image(
//                        painter = painterResource(id),
//                        contentDescription = null,
//                        alpha = if (pic == id) 1f else 0.8f,
//                        modifier = Modifier
//                            .size(30.dp)
//                            .clickable {
//                                todo.image = id
//                                pic = id
//                            }
//                    )
//                    if (pic == id) {
//                        Box(
//                            modifier = Modifier
//                                .padding(top = 4.dp)
//                                .width(30.dp)
//                                .height(1.dp)
//                                .background(Color.Red)
//                                .align(Alignment.CenterHorizontally)
//                        )
//                    } else {
//                        Spacer(Modifier.height(4.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun TodoListItem(todo: Todo, todoViewModel: TodoViewModel) {
//    Row(modifier = Modifier.padding(bottom = 4.dp)
//        .clickable{todoViewModel.toggle()}) {
//        Text(todo.info, modifier = Modifier.weight(1f))
//        Image(
//            painter = painterResource(todo.image), null,
//            modifier = Modifier.size(30.dp)
//        )
//    }
//}
//
//@Composable
//fun TodoList(todoViewModel: TodoViewModel) {
//    LazyColumn {
//        items(todoViewModel.list) {
//            TodoListItem(it,todoViewModel)
//        }
//    }
//}
//
//@Composable
//fun TodoScreen(todoViewModel: TodoViewModel) {
//    Column {
//        //title
//        TitleRow(todoViewModel)
//        //list
//        TodoList(todoViewModel)
//        //btn
//
//    }
//}


data class Todo(val info: String = "", val image: Int = images[0]) {
    companion object {
        val images = listOf(R.drawable.a1, R.drawable.a3)
    }

}


data class TodoUiState(
    val list: List<Todo> = emptyList(),
    val index: Int = -1,
    val current: Todo = Todo(),
    val isEdit: Boolean = false
)

sealed class TodoIntent() {
    data class OnValueChange(val info: String) : TodoIntent()
    data class ImageClick(val id: Int) : TodoIntent()
    object Click : TodoIntent()
    data class ListItemClick(val todo: Todo) : TodoIntent()
    object RandomBtnClick : TodoIntent()
    object BtnChange : TodoIntent()
    object BtnDel : TodoIntent()
}


class TodoViewModel : ViewModel() {
    var uiState by mutableStateOf(TodoUiState())
        private set

    private fun add(todo: Todo) {
        if (todo.info.isBlank()) {
            return
        }
        uiState = uiState.copy(list = uiState.list + todo)
    }

    private fun del(todo: Todo) {
        uiState = uiState.copy(list = uiState.list - todo)
        resetCurrent()
    }

    private fun change() {
        uiState = uiState.copy(list = uiState.list.map {
            if (it == uiState.list[uiState.index]) {
                uiState.current
            } else {
                it
            }
        })
    }

    private fun setCurrent(todo: Todo) {
        val indexOf = uiState.list.indexOf(todo)
        uiState = uiState.copy(index = indexOf)
        uiState = uiState.copy(current = todo)
    }

    private fun setCurrent(info: String) {
        uiState = uiState.copy(current = uiState.current.copy(info = info))
    }

    private fun setCurrent(id: Int) {
        uiState = uiState.copy(current = uiState.current.copy(image = id))
    }


    private fun resetCurrent() {
        uiState = uiState.copy(index = -1)
        uiState = uiState.copy(current = Todo())
    }

    private fun toggle() {
        uiState = uiState.copy(isEdit = !uiState.isEdit)
    }


    fun onIntent(todoIntent: TodoIntent) {
        when (todoIntent) {
            is TodoIntent.OnValueChange -> setCurrent(todoIntent.info)
            is TodoIntent.ImageClick -> setCurrent(todoIntent.id)
            is TodoIntent.Click -> {
                add(uiState.current)
                resetCurrent()
            }

            is TodoIntent.ListItemClick -> {
                toggle()
                setCurrent(todoIntent.todo)
            }

            is TodoIntent.RandomBtnClick -> {
                val first = Todo.images.shuffled().first()
                val todo = Todo(info = "hello world${Random.nextInt()}", image = first)
                add(todo)
            }

            is TodoIntent.BtnChange -> {
                change()
                toggle()
                resetCurrent()
            }

            is TodoIntent.BtnDel -> {
                del(uiState.list[uiState.index])
                toggle()
                resetCurrent()
            }
        }
    }


}


@Composable
fun Edit(
    uiState: TodoUiState,
    onIntent: (TodoIntent) -> Unit,
    todoIntent: TodoIntent,
    content: @Composable () -> Unit
) {
    val current = LocalSoftwareKeyboardController.current
    Column {
        Row {
            TextField(
                value = uiState.current.info,
                onValueChange = { onIntent(TodoIntent.OnValueChange(it)) },
                modifier = Modifier
                    .padding(end = 5.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    onIntent(todoIntent)
                    current?.hide()
                })
            content()
        }
        if (uiState.current.info.isNotBlank()) {
            LazyRow {
                items(Todo.images) {
                    Column {
                        Image(
                            painter = painterResource(it),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    onIntent(TodoIntent.ImageClick(it))
                                },
                            alpha = if (it == uiState.current.image) 1f else 0.7f
                        )
                        if (it == uiState.current.image) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(30.dp)
                                    .height(1.dp)
                                    .background(Color.Red)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                    }
                }
            }
        }

    }
}

@Composable
fun ClickBtn(uiState: TodoUiState, onIntent: (TodoIntent) -> Unit) {
    val current = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            current?.hide()
            onIntent(TodoIntent.Click)
        }, enabled = uiState.current.info.isNotBlank()
    ) {
        Text("click", textAlign = TextAlign.Center)
    }
}


@Composable
fun TodoScreen(uiState: TodoUiState, onIntent: (TodoIntent) -> Unit) {
    Column(modifier = Modifier.padding(top = 30.dp)) {
        //title
        Row(
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            if (uiState.isEdit) {
                Text(
                    "Edit", textAlign = TextAlign.Center, modifier = Modifier.weight(1f)
                )
            } else {
                Edit(uiState, onIntent, todoIntent = TodoIntent.Click) {
                    ClickBtn(uiState, onIntent)
                }
            }
        }
        //list
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(uiState.list) { index, it ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 4.dp)
                        .clickable {
                            onIntent(TodoIntent.ListItemClick(it))
                        }) {

                    if (uiState.isEdit && uiState.index == index) {
                        Edit(uiState, onIntent, todoIntent = TodoIntent.BtnChange) {
                            val (a, b) = Todo.images
                            Row {
                                Image(
                                    painter = painterResource(a),
                                    null,
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .size(30.dp)
                                        .clickable { onIntent(TodoIntent.BtnChange) })
                                Image(
                                    painter = painterResource(b),
                                    null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable { onIntent(TodoIntent.BtnDel) })
                            }
                        }
                    } else {
                        Text(it.info, modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(it.image),
                            null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
        //btn
        Button(
            modifier = Modifier.fillMaxWidth(), onClick = { onIntent(TodoIntent.RandomBtnClick) }) {
            Text("random add", textAlign = TextAlign.Center)
        }


    }
}


data class City(var name: String)


@Composable
fun City() {
    val saver = run {
        val nameKey = "name"
        mapSaver(save = {
            mapOf(nameKey to it.name)
        }, restore = {
            City(it[nameKey] as String)
        })
    }
    var city by rememberSaveable(stateSaver = saver) {
        mutableStateOf(City("hls"))
    }

    Surface {
        Column(modifier = Modifier.padding(top = 30.dp)) {
            Text(city.name)
            Button(onClick = {
                city = city.copy(name = "asdas")
            }) {
                Text("click")
            }
        }

    }

}


@Composable
fun Demo() {
    Column {
        Text("hls")
        CompositionLocalProvider(LocalContentColor provides Color.Red) {
            Text("hls")
        }
    }

}


data class Elevation(val ele: Dp = 0.dp)


val LocalElevation = compositionLocalOf { Elevation() }

object Ele {
    val high
        get() = Elevation(10.dp)
}

@Composable
fun MyCard(elevation: Dp = LocalElevation.current.ele) {
    Surface(
        shadowElevation = elevation,
        modifier = Modifier.size(200.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
    ) {

    }
}

@Composable
fun Demo1() {
    Column {
        CompositionLocalProvider(LocalElevation provides Ele.high) {
            MyCard()
        }
        MyCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldExample() {
    Scaffold(
        // 1. 顶部栏插槽
        topBar = {
            TopAppBar(
                title = { Text("标题栏") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        // 2. 底部栏插槽
        bottomBar = {
            BottomAppBar {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "底部导航栏"
                )
            }
        },
        // 3. 悬浮按钮插槽
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 执行操作 */ }) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        },
        // 4. 悬浮按钮位置（可选）
        floatingActionButtonPosition = FabPosition.End,

        // 5. 提示条插槽（通常配合 SnackbarHost 使用）
        snackbarHost = { /* SnackbarHost(hostState) */ }) { innerPadding ->
        // 6. 主内容区域
        // 注意：必须使用 innerPadding，否则内容会被 topBar 或 bottomBar 遮挡
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            Text("这是主内容区域", modifier = Modifier.padding(16.dp))
        }
    }
}


@Composable
fun Demo4() {
    var flag by remember { mutableStateOf(false) }
    val color by animateColorAsState(if (flag) Color.Red else Color.Blue)
    Surface(modifier = Modifier.size(300.dp), color = color) {
        Button(onClick = { flag = !flag }) {
            Text("click")
        }
    }
}


@Composable
fun Demo5() {
    Column(modifier = Modifier.padding(top = 30.dp)) {
        Text(
            "edit", modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(0.2f))
        )
        var flag by remember { mutableStateOf(false) }
        AnimatedVisibility(
            visible = flag, enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ), exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 250, easing = FastOutSlowInEasing
                )
            )
        ) {
            Text(
                "hello world", modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red.copy(0.2f))
            )
        }

        Button(onClick = { flag = !flag }) {
            Text("click")
        }
    }
}


@Composable
fun Demo6() {
    var flag by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .background(Color.Black.copy(alpha = 0.1f))
            .animateContentSize()
    ) {
        Text(
            "edit",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        if (flag) {
            Text(
                "hello world",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
        Button(onClick = { flag = !flag }) {
            Text("click")
        }
    }
}


@Composable
fun Demo7() {

    var flag by remember { mutableIntStateOf(0) }
    val transition = updateTransition(flag)
    val color by transition.animateColor(label = "color") {
        if (it == 0) {
            Color.Red
        } else {
            Color.Blue
        }
    }

    val left by transition.animateDp(
        transitionSpec = {
            if (flag == 0) {
                spring(
                    stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioLowBouncy
                )
            } else {
                spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy)
            }
        }) {
        if (it == 0) {
            0.dp
        } else {
            220.dp
        }
    }

    val right by transition.animateDp(
        transitionSpec = {
            if (flag == 0) {
                spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy)
            } else {
                spring(
                    stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioLowBouncy
                )
            }
        }) {
        if (it == 0) {
            220.dp
        } else {
            460.dp
        }
    }


    Box(modifier = Modifier.background(color.copy(alpha = 0.1f))) {
        Box(
            modifier = Modifier
                .offset(left)
                .width(right - left)
                .height(100.dp)
                .border(
                    border = BorderStroke(1.dp, color), shape = RoundedCornerShape(15.dp)
                )
        )
        Row {
            Text(
                "hello",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .width(200.dp)
                    .height(100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .clickable { flag = 0 })
            Text(
                "world",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .clickable { flag = 1 })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySideDrawerApp() {
    // 1. 初始化状态：管理菜单的打开与关闭
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 2. 最外层容器
    ModalNavigationDrawer(
        drawerState = drawerState,
        // 允许手势滑动开关（默认开启）
        gesturesEnabled = true,
        // 3. 菜单面板的内容
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp) // 设置菜单宽度
            ) {
                Text(
                    "菜单标题",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("首页") },
                    selected = true,
                    onClick = { /* 处理点击 */ },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("设置") },
                    selected = false,
                    onClick = { /* 处理点击 */ },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }) {
        // 4. 页面的主内容
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("我的应用") }, navigationIcon = {
                    IconButton(onClick = {
                        // 5. 点击图标打开菜单 (需要在协程中执行)
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
            }) { padding ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("右滑或点击左上角打开菜单")
            }
        }
    }
}


@Composable
fun Demo8() {
    val flag = rememberInfiniteTransition()
    val animateFloat by flag.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f at 0
                1f at 1000
                0f at 2000
            }, repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .background(Color.Red.copy(alpha = animateFloat))
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernSwipeToDeleteList() {
    // 1. 模拟数据（使用 id 保证 key 的唯一性，防止动画闪烁）
    val items = remember {
        mutableStateListOf(*(1..10).map { "项目 $it" }.toTypedArray())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = items,
            key = { it } // 关键：生产环境建议用唯一 ID
        ) { item ->
            // 2. 初始化状态（按照最新 API 规范）

            val dismissState = rememberSwipeToDismissBoxState(
                initialValue = SwipeToDismissBoxValue.Settled,
                positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
            )


            // 3. 监听状态变化执行删除逻辑
            // 只有当滑动真正完成（currentValue 改变）时，才触发数据移除
//            LaunchedEffect(dismissState.currentValue) {
//                snapshotFlow { dismissState.currentValue }
//                    .collect { currentValue ->
//                        if (currentValue == SwipeToDismissBoxValue.EndToStart) {
//                            // 此时动画已彻底完成，卡片已滑出
//                            items.remove(item)
//                        }
//                    }
//            }

            // 4. 组件容器
            SwipeToDismissBox(
                state = dismissState,
                modifier = Modifier.animateItem(), // 重要：让下方 Item 平滑上移
                enableDismissFromStartToEnd = false, // 禁用右滑
                backgroundContent = {
                    // 背景：滑动时显示的底色和图标
                    val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        Color(0xFFFFDAD4) // 浅红色提示
                    } else {
                        Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color, RoundedCornerShape(12.dp))
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = if (color == Color.Transparent) Color.Gray else Color.Red
                        )
                    }
                }
            ) {
                // 5. 前景：具体的卡片内容
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(item) },
                        supportingContent = { Text("向左滑动删除该条目") },
                        leadingContent = {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Demo9() {
    val list = remember { mutableStateListOf("") }
    for (i in 1..10) {
        list.add("hello$i")
    }
    LazyColumn {
        items(list, key = { it }) {
            val state = rememberSwipeToDismissBoxState(
                initialValue = SwipeToDismissBoxValue.Settled,
                positionalThreshold = { it ->
                    it * 0.5f
                })

            SwipeToDismissBox(
                state = state,
                modifier = Modifier.animateItem(),
                enableDismissFromStartToEnd = false,
                onDismiss = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        list -= it
                    }
                },
                backgroundContent = {
                    val color = if (state.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        Color(0xFFFFDAD4) // 浅红色提示
                    } else {
                        Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .background(color)
                            .fillMaxSize()
                    ) {

                    }
                }
            ) {
                Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItem(
    item: Any,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    // 监听状态变化（另一种方式）
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.White
                )
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

// 使用示例
@Composable
fun TodoList() {
    val items = remember { mutableStateListOf("任务1", "任务2", "任务3") }

    LazyColumn {
        items(items.size) { index ->
            SwipeToDeleteItem(
                item = items[index],
                onDelete = { items.removeAt(index) }
            ) {
                Text(text = items[index], fontSize = 18.sp)
            }
        }
    }
}

const val Tag = "hello"

@Composable
fun Demo10() {
    var count by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .padding(top = 30.dp)
            .size(50.dp)
            .background(Color.Black.copy(alpha = 0.1f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { Log.i(Tag, "长摁") },
                    onPress = { Log.i(Tag, "压住") },
                    onTap = { Log.i(Tag, "单击") },
                    onDoubleTap = { Log.i(Tag, "双击") }
                )
            }) {
        Text(count.toString())
    }
}


@Composable
fun Demo11() {
    var count by remember { mutableIntStateOf(0) }
    val state = rememberScrollState()
    val rememberScrollableState = rememberScrollableState {
        count += it.toInt()
        it
    }
    Row(modifier = Modifier.padding(top = 30.dp)) {
        Column(
            modifier = Modifier
                .scrollable(state = rememberScrollableState, orientation = Orientation.Vertical)
                .verticalScroll(state)
        ) {
            repeat(100) {
                Text("hello $it")
            }
        }
        Text(count.toString())
    }
}


@Composable
fun Demo12() {

    Box(
        modifier = Modifier
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(50.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "hello world", textAlign = TextAlign.Center,
                        modifier = Modifier
                            .size(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    0f to Color.Gray,
                                    200f to Color.White
                                )
                            )
                    )
                }
            }
        }
    }
}


fun Modifier.superDrag() = composed {
    var x by remember { mutableIntStateOf(0) }
    var y by remember { mutableIntStateOf(0) }

    offset { IntOffset(x, y) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                x += dragAmount.x.roundToInt()
                y += dragAmount.y.roundToInt()
            }
        }
}

@Composable
fun Demo13() {

    Text(
        "hello world", modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxSize()
            .superDrag()
    )

}


@SuppressLint("RememberInComposition")
@Composable
fun Demo14() {

    var checked by remember { mutableStateOf(true) }

    // 系统自带，自带滑动动画和点击切换
//    Switch(
//        checked = checked,
//        onCheckedChange = { checked = it }
//    )

    val width = 96.dp
    val w = with(LocalDensity.current) { width.toPx() } / 2
    val offset = remember { Animatable(0f) }



    Box(
        modifier = Modifier
            .padding(30.dp)
            .width(width)
            .height(48.dp)
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .width(width / 2)
                .height(48.dp)
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .background(Color.White)
                .draggable(
                    state = rememberDraggableState {
                        runBlocking {
                            var f = offset.value + it
                            if (f >= w) f = w else if (f <= 0) f = 0f
                            offset.snapTo(f)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offset.value > w * 0.5) {
                            offset.animateTo(w)
                        } else {
                            offset.animateTo(0f)
                        }
                    }
                )
        ) { }
    }

}


@Composable
fun Demo15() {

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
        rotation += rotationChange
    }


    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Red)
                .align(Alignment.CenterVertically)
                .transformable(state)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y,
                    rotationZ = rotation,
                )

        )
    }


}


//@Serializable
//object Home {
//    val name = "home"
//}
//
//
//@Serializable
//data class Detail(var info: String) {
//}
//
//
//@Composable
//fun HomeScreen() {
//    Column {
//        Text("welcome to home")
//        val nav = LocalNav.current
//
//        Button(onClick = { nav.navigate(DetailScreen("hello") }) {
//            Text("click")
//        }
//    }
//}
//
//@Composable
//fun DetailScreen(info: String) {
//    Column {
//        Text(info)
//        Button(onClick = { click }) {
//            Text("click")
//        }
//    }
//}


@Composable
fun Demo16() {

    val state = rememberNavController()
    var name by remember { mutableStateOf("home") }


}


@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "page1"
    ) {
        composable("page1") {
            Page1(navController)
        }
        composable("page2") {
            Page2(navController)
        }
    }
}

@Composable
fun Page1(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("这是第 1 页", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("page2")
            }
        ) {
            Text("跳转到第 2 页")
        }
    }
}

@Composable
fun Page2(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("这是第 2 页", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("返回上一页")
        }
    }
}


@Serializable
object Home

@Composable
fun HomeScreen(click: (String) -> Unit) {
    Column {
        Text("home")
        Button(onClick = { click("hello world") }) {
            Text("进入详情")
        }
    }
}

@Serializable
data class Detail(val name: String)

@Composable
fun DetailScreen(name: String, click: () -> Unit) {
    Column {
        Text(name)
        Button(onClick = click) {
            Text("back")
        }
    }
}


@Composable
fun Page() {
    val state = rememberNavController()
    NavHost(
        navController = state,
        modifier = Modifier.padding(top = 30.dp),
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen { state.navigate(Detail(it)) }
        }

        composable<Detail>(
            deepLinks = listOf(
                navDeepLink<Detail>(basePath = "app://detail.com")
            )
        ) {
            val toRoute = it.toRoute<Detail>()
            DetailScreen(toRoute.name) {
                state.popBackStack()
            }
        }
    }
}


@Composable
fun Handel(
    backPressedDispatcher: OnBackPressedDispatcher,
    back: () -> Unit
) {

    val func = rememberUpdatedState(back)

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            func.value()
        }
    }

    DisposableEffect(backPressedDispatcher) {
        backPressedDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}


@Composable
fun Page1(backPressedDispatcher: OnBackPressedDispatcher) {
    var flag by remember { mutableStateOf(false) }

    Column {
        Switch(checked = flag, onCheckedChange = {
            flag = !flag
        })
        Text(if (flag) "no back" else "back")
    }

    if (flag) {
        Handel(backPressedDispatcher) {
            Log.i(Tag, "no back")
        }
    }


}


suspend fun b() {
    Thread.sleep(10000)
    Log.i("car", "hello wrld")
}


class Repository {

    suspend fun get_name(): String {
        Thread.sleep(3000)
        return "hello world"
    }
}

class QViewModel : ViewModel() {
    private val repository = Repository()

    var info = MutableLiveData<String>()
        private set

    fun get_info() {
        viewModelScope.launch(context = Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
            info.value = repository.get_name()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }


}


@Composable
fun q1(qViewModel: QViewModel) {
    val observeAsState = qViewModel.info.observeAsState()
    Column {
        Text(observeAsState.value ?: "")
        Button(onClick = { qViewModel.get_info() }) {
            Text("click")
        }
    }
}

suspend fun get_name(): String {
    delay(1000)
    return "hello world"
}

@Composable
fun q2() {
    runBlocking {
        val launch = launch {
            get_name()
        }
        launch
        launch.join()
        val async = async {
            get_name()
        }
        async
        val await = async.await()
        println(await)
    }
//
//    withContext(NonCancellable){
//
//    }
//
//    withTimeoutOrNull(1000){
//
//    }


}


//suspend fun demo14(){
//    val handle = CoroutineExceptionHandler { context, throwable ->
//        Log.i("car",throwable+throwable.suppressed.contentToString())
//    }
//
//    GlobalScope.launch(handle) {
//        launch {
//            throw NullPointerException()
//        }
//        launch {
//            throw IndexOutOfBoundsException()
//        }
//        launch {
//            throw NoWhenBranchMatchedException()
//        }
//    }.join()
//
//}


fun demo133(): Flow<Int> {
    return flow {
        for (i in 1..10) {
            delay(1000)
            emit(i)
        }
    }.transform {
        emit(it)
    }.take(2)
        .onCompletion { }
}


fun demo12312() = runBlocking {
    launch {
        demo133().cancellable().collect { println(it) }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun demo124() {

    val produce = CoroutineScope(Dispatchers.IO).produce {
        for (i in 0..10) {
            send(i)
        }
    }


    runBlocking {
        launch {
            for (i in produce) {
                println(i)
            }
        }
    }

    val myChannel = Channel<Int>()

// 2. 模拟 produce 的行为
    CoroutineScope(Dispatchers.IO).launch {
        try {
            for (i in 1..5) {
                delay(100)
                myChannel.send(i)
            }
        } finally {
            myChannel.close() // 手动关闭，逻辑更清晰
        }
    }
//    private val _sensorData = MutableSharedFlow<List<Double>>()
//    val sensorData = _sensorData.asSharedFlow() // 暴露为只读
//
//    // 2. 发送解析后的数据
//    suspend fun parseAndEmit(raw: String) {
//        val data = raw.split(",").map { it.toDouble() }
//        _sensorData.emit(data) // 广播给所有监听者
//    }
//
//    select<Unit> {
//        response.onAwait { data ->
//            println("收到硬件回复: $data")
//        }
//        onTimeout(500) {
//            println("蓝牙响应超时，尝试重发...")
//        }
//    }
}


@OptIn(ObsoleteCoroutinesApi::class)
fun demo12423() {

    val actor = CoroutineScope(Dispatchers.IO).actor<Int> {
        while (true) {
            println(receive())
        }
    }


    runBlocking {
        launch {
            for (i in 0..10) {
                actor.send(i)
            }
        }
    }


}


fun demo1242323() {
    runBlocking {

        launch {

            val async1 = async(Dispatchers.IO) {
                delay(Random.nextLong(5))
                "hello world"
            }

            val async = async {
                delay(2)
                "default action"
            }

            val select = select {
                async1.onAwait { it }
                async.onAwait { it }
            }
            Log.i("car", select)
        }.join()
    }

}


fun asdasd() {
    runBlocking {

        val listOf = listOf(Channel<Int>(), Channel<Int>())
        launch {
            for (i in 0..10) {
                delay(100)
                listOf[1].send(100)
            }
        }

        launch {
            for (i in 0..10) {
                delay(200)
                listOf[1].send(200)
            }
        }

        for (i in 0..10) {
            val select = select {
                listOf.forEach {
                    it.onReceive {
                        it
                    }
                }
            }
            println(select)
        }

    }

}


data class FeedItem(val id: Long, val title: String, val content: String, val time: String)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedScreen() {
    // 2. 状态：维护一个动态列表
    var feedItems by remember { mutableStateOf(listOf<FeedItem>()) }

    // 3. 模拟实时产生新数据的“喂料”过程
    LaunchedEffect(Unit) {
        var count = 0L
        while (true) {
            val newItem = FeedItem(
                id = count++,
                title = "通知 #$count",
                content = "这是一条实时推送到 Feed 的演示消息。",
                time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            )
            // 将新项放在列表最前面（Index 0）
            feedItems = listOf(newItem) + feedItems
            delay(2000) // 每 2 秒更新一次
        }
    }

    // 4. UI 布局
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "实时消息 Feed",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // LazyColumn 相当于 RecyclerView
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // items 使用 id 作为 key，这样列表刷新时性能最好
            items(items = feedItems, key = { it.id }) { item ->
                FeedCard(item)
            }
        }
    }
}

@Composable
fun FeedCard(item: FeedItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = item.time, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.content, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}


@Entity
data class Person(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val name: String = "",
    @ColumnInfo
    val age: Int = 0
)


@Dao
interface PersonMapper {
    @Query("select * from person where id= :id")
    fun getId(id: Long): Person

    @Insert
    fun add(person: Person): Long

    @Delete
    fun del(person: Person)

}


@Database(entities = [Person::class], version = 1)
abstract class Store : RoomDatabase() {
    abstract fun personMapper(): PersonMapper

    companion object {

        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var instance: Store? = null
        fun getInstance(context: Context): Store {
            return instance ?: synchronized(this) {
                val store = instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    Store::class.java,
                    "db"
                ).build()
                instance = store
                store
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun demo123(context: Context) {
    var obj by remember { mutableStateOf(Person()) }
    val scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        val store = Store.getInstance(context)
        val personMapper = store.personMapper()
        obj= personMapper.getId(1)
    }

    Column {
        Text(obj.toString())
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                val store = Store.getInstance(context)
                val personMapper = store.personMapper()
                val add = personMapper.add(person = Person(name = "hls", age = 18))
                obj = personMapper.getId(add)
            }
        }) {
            Text("click")
        }
    }


}






































