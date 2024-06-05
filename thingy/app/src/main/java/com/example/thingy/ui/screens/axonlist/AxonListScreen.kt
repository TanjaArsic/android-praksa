package com.example.thingy.ui.screens.axonlist

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.thingy.R
import com.example.thingy.domain.model.Axon
import com.example.thingy.domain.model.FlowItem
import com.example.thingy.domain.model.Stream
import com.example.thingy.domain.model.StreamType
import com.example.thingy.ui.Colors
import com.example.thingy.ui.Colors.Companion.toColor
import com.example.thingy.ui.NetworkErrorScreen
import com.example.thingy.ui.ShimmerEffect
import com.example.thingy.ui.screens.axonlist.AxonListViewModel.*
import com.example.thingy.ui.screens.axonlist.AxonListViewModel.Events.*


@Composable
fun IsLoadingState(
    viewModel: AxonListViewModel
) {

    val isLoading by remember {
        viewModel.isLoading
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            LoadingAxonItem()
        }
    }
}

@Composable
fun IsErrorState(
    viewModel: AxonListViewModel
) {
    val isError by remember {
        viewModel.isError
    }
    isError?.let { errorText ->

        if (errorText.isNotEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                NetworkErrorScreen(errorText) { viewModel.onRetryClicked() }
            }
        }
    }
}

@Composable
fun AxonScreen(
    navController: NavController,
) {
    val axonListViewModel = hiltViewModel<AxonListViewModel>()

    AxonView(axonListViewModel, navController)
    IsErrorState(axonListViewModel)
    IsLoadingState(axonListViewModel)
}

@Composable
fun AxonView(
    viewModel: AxonListViewModel,
    navController: NavController
) {
    val axonList by remember {
        viewModel.axonList
    }

    val event by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(event) {
        when (event) {
            is NavigateToAxonDetails -> {
                val axonId = (event as NavigateToAxonDetails).axonId
                navController.navigate("details/$axonId")
                viewModel.events.emit(null)
            }
            else -> {}
        }
    }

    Box {
        if (axonList.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(items = axonList) { _, item ->
                    AxonItem(
                        axon = item,
                        viewModel = viewModel,
                        onItemClicked = { axonId ->
                            viewModel.onAxonSelected(axonId)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AxonItem(
    axon: Axon,
    viewModel: AxonListViewModel,
    onItemClicked: (String) -> Unit,
) {

    val streamList by remember {
        viewModel.streamListByAxon
    }

    LaunchedEffect(axon) {
        axon.let {
            viewModel.fetchStreamsForAxon(it)
        }
    }
    Box(modifier = Modifier.fillMaxWidth())
        {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onItemClicked(axon.uid) }
                ),
            shape = RoundedCornerShape(29.dp),

            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    axon.name?.let {
                        Text(
                            text = it.replace(".", " "),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.W200,
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (axon.validity == "active") {
                        Image(
                            painter = painterResource(id = R.drawable.checkmark),
                            contentDescription = "Check Mark",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Detailed info about axon: ")
                Spacer(modifier = Modifier.height(15.dp))

                val temperature = streamList.find { it.type == StreamType.TEMPERATURE }
                    ?.let { getStreamValueAndDescription(viewModel, it) }
                val humidity = streamList.find { it.type == StreamType.HUMIDITY }
                    ?.let { getStreamValueAndDescription(viewModel, it) }
                val eAqi = streamList.find { it.type == StreamType.EAQI }
                    ?.let { getStreamValueAndDescription(viewModel, it) }
                val pressure = streamList.find { it.type == StreamType.PRESSURE }
                    ?.let { getStreamValueAndDescription(viewModel, it) }

                AirQualityIndexWidget(
                    temperature = temperature,
                    humidity = humidity,
                    eAqi = eAqi,
                    pressure = pressure
                )
            }
        }
    }
}

@Composable
fun LoadingAxonItem() {
    LazyColumn {
        items(2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillParentMaxHeight(0.5f)
                    .background(Color.Black.copy(alpha = 0.1f))
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(color = Color.White)
                        ) {
                            ShimmerEffect(Colors.SHIMMER_SILVER.color, 35.dp)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = Color.White)
                        ) {
                            ShimmerEffect(Colors.SHIMMER_SILVER.color, 18.dp)
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(29.dp))
                                .background(color = Color.White)
                        ) {
                            ShimmerEffect(Colors.SHIMMER_SILVER.color, 200.dp)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AirQualityIndexWidget(
    temperature: Pair<String?, String?>?,
    humidity: Pair<String?, String?>?,
    eAqi: Pair<String?, String?>?,
    pressure: Pair<String?, String?>?
) {
    val eAqiVal = eAqi?.first.orEmpty()
    val eAqiText = eAqi?.second.orEmpty()
    val color = eAqiVal.toColor().color

    Card(shape = RoundedCornerShape(29.dp)) {
        Box(
            modifier = Modifier
                .background(color)
                .padding(16.dp)
                .clip(RoundedCornerShape(1.dp))
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.data_aqi),
                        contentDescription = "AQI Icon",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        if (eAqiVal == "") {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Colors.FONT.color,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = eAqiVal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 30.sp,
                                color = Colors.FONT.color
                            )
                        }
                        Text(
                            text = eAqiText,
                            fontSize = 17.sp,
                            color = Colors.FONT.color
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth()
//                        horizontalArrangement = Arrangement.Center,
//                    verticalArrangement = Arrangement.Center
                ) {
                    if (temperature != null) {
                        ShowItem(
                            icon = R.drawable.contrast,
                            param = temperature
                        )
                    }
                    Spacer(Modifier.width(15.dp))
                    if (humidity != null) {
                        ShowItem(
                            icon = R.drawable.water,
                            param = humidity
                        )
                    }
                    Spacer(Modifier.width(15.dp))
                    if (pressure != null) {
                        ShowItem(
                            icon = R.drawable.pressure,
                            param = pressure,
                            size = 32.dp,
                            pressure = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowItem(
    @DrawableRes icon: Int,
    param: Pair<String?, String?>?,
    size: Dp = 35.dp,
    pressure: Boolean = false
) {
    val paramVal = param?.first ?: ""
    val paramText = param?.second ?: ""

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = painterResource(icon),
                contentDescription = "Icon",
                modifier = Modifier.size(size)
            )
            if (pressure) Spacer(Modifier.height(20.dp))

            if (paramVal == "") {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.Gray,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = paramVal,
                    color = Colors.FONT.color,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }

        if (pressure)
            Text(text = "Pressure in hPa", color = Colors.FONT.color)
        else
            Text(text = paramText, color = Colors.FONT.color)

    }
}

@Composable
fun getStreamValueAndDescription(
    viewModel: AxonListViewModel,
    stream: Stream
): Pair<String?, String?>? {

    stream.uId?.let { streamId ->
        viewModel.fetchLatestStreamData(streamId)

        val data by remember {
            viewModel.streamFlowData
        }

        val flowItem = data[streamId] as? FlowItem
        return flowItem?.value to stream.description
    }
    return null
}
