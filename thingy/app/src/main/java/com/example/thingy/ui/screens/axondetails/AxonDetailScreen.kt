import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.thingy.R
import com.example.thingy.domain.model.AirQualityValues
import com.example.thingy.domain.model.FlowItem
import com.example.thingy.domain.model.Stream
import com.example.thingy.domain.model.StreamType
import com.example.thingy.ui.Colors
import com.example.thingy.ui.Colors.Companion.toColor
import com.example.thingy.ui.NetworkErrorScreen
import com.example.thingy.ui.ShimmerEffect
import com.example.thingy.ui.screens.axondetails.AxonDetailViewModel
import com.example.thingy.ui.screens.axondetails.AxonDetailViewModel.Events.NavigateBack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IsLoadingState(
    viewModel: AxonDetailViewModel
) {

    val isLoading by remember {
        viewModel.isLoading
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.SHIMMER_LIGHT_BLUE.color),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Blurred Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(35.dp))

                CircularProgressIndicator(
                    color = Colors.SHIMMER_LIGHT_BLUE.color,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(25.dp),
                    strokeWidth = 9.5.dp
                )
                Spacer(modifier = Modifier.height(15.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    maxItemsInEachRow = 3,
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(8) {
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .width(170.dp)
                                .height(85.dp)
                                .animateContentSize(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            ShimmerEffect(Colors.STREAM_ITEM.color, 90.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IsErrorState(
    viewModel: AxonDetailViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StreamScreen(
    navController: NavController
) {
    val axonDetailViewModel = hiltViewModel<AxonDetailViewModel>()

    StreamView(axonDetailViewModel, navController)
    IsErrorState(axonDetailViewModel)
    IsLoadingState(axonDetailViewModel)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StreamView(
    viewModel: AxonDetailViewModel,
    navController: NavController
) {
    val streamList by remember {
        viewModel.streamListByAxon
    }

    val axon by remember {
        viewModel.axon
    }

    val event by viewModel.events.collectAsState(null)

    LaunchedEffect(event) {

        when (event) {
            NavigateBack -> {
                navController.popBackStack()
                viewModel.events.emit(null)
            }

            else -> {}
        }
    }

    LaunchedEffect(axon) {
        axon?.let {
            viewModel.fetchStreamsForAxon(it)
        }
    }
    val image = ImageBitmap.imageResource(R.drawable.background)
    val brush = remember(image) {
        ShaderBrush(
            ImageShader(
                image = image,
                tileModeX = TileMode.Repeated,
                tileModeY = TileMode.Repeated
            )
        )
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(brush)
    ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Blurred Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {

                Button(onClick = { viewModel.navigateBack() }) {
                    Text("Back")
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    streamList.filter { it.type == StreamType.FPM }.forEach { stream ->
                        FPMGauge(stream, viewModel)
                    }
                    AirQualityCardsRow(listOfStreams = streamList, axonDetailViewModel = viewModel)
                }
            }

        }
    }


    @Composable
    fun getStreamData(
        stream: Stream,
        axonDetailViewModel: AxonDetailViewModel
    ): Pair<String?, String?>? {

        stream.uId?.let { streamId ->
            axonDetailViewModel.fetchLatestStreamData(streamId)

            val data by remember {
                axonDetailViewModel.streamFlowData
            }

            val flowItem = data[streamId] as? FlowItem
            return flowItem?.value to flowItem?.timestamp
        }

        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StreamItem(
        stream: Stream,
        axonDetailViewModel: AxonDetailViewModel,
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        val latest = getStreamData(stream, axonDetailViewModel) ?: ("" to "")
        val latestTimestamp =
            latest.second?.let { parseTime(it, "HH:mm", "yyyy-MM-dd HH:mm:ss.SSS") }

        Card(
            modifier = Modifier
                .padding(4.dp)
                .width(170.dp)
                .animateContentSize()
                .clickable { isExpanded = !isExpanded },
            shape = RoundedCornerShape(8.dp),
        ) {

            Column(
                modifier = Modifier
                    .background(Colors.STREAM_ITEM.color)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.TopStart
                    ) {
                        stream.name?.let {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 17.sp,
                                maxLines = 1
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            color = Color.White,
                            text = latestTimestamp ?: ""
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                val latestValue = latest.first
                if (latestValue == null)
                    CircularProgressIndicator(color = Color.White)
                else
                    Text(
                        text = latestValue,
                        color = Color.White,
                        fontSize = 25.sp,
                        maxLines = 1
                    )

                Spacer(modifier = Modifier.height(4.dp))

                val airQualityValues = stream.airQualityData

                airQualityValues?.let {

                    if (latestValue != null) {
                        AirQualityBar(
                            thresholdValues = airQualityValues,
                            value = latestValue
                        )
                    }
                }
                AnimatedVisibility(visible = isExpanded) {
                    Text(
                        text = stream.description.orEmpty(),
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.White
                    )
                }
            }
        }
    }

    @Composable
    fun FPMGauge(
        stream: Stream,
        axonDetailViewModel: AxonDetailViewModel
    ) {
        val fpmValue = getStreamData(stream, axonDetailViewModel)?.first?.toDoubleOrNull()
        val fpmQualityValue = stream.airQualityData
        val boundedValue = fpmQualityValue?.let {
            fpmValue?.coerceIn(it.minValue, fpmQualityValue.maxValue)
        }
        val sweepAngle =
            ((fpmQualityValue?.let {
                boundedValue?.minus(it.minValue)
            })?.div((fpmQualityValue.maxValue - fpmQualityValue.minValue)))?.times(
                300.0
            )

        val level = boundedValue?.let {

            toRangeStatus(
                boundedValue,
                fpmQualityValue
            )
        }

        val color = level?.toColor()?.color

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {

                drawArc(
                    color = Colors.SHIMMER_LIGHT_BLUE.color,
                    startAngle = 120f,
                    sweepAngle = 300f,
                    useCenter = false,
                    size = size,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )


                if (color != null && sweepAngle != null) {
                    drawArc(
                        color = color,
                        startAngle = 120f,
                        sweepAngle = sweepAngle.toFloat(),
                        useCenter = false,
                        size = size,
                        style = Stroke(width = 30f, cap = StrokeCap.Round)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PM2.5",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
                val displayValue = ((fpmValue?.times(10))?.roundToInt() ?: 0) / 10

                Text(
                    text = displayValue.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.White
                    )
                )
                if (level != null && color != null) {
                    Text(
                        text = level,
                        style = MaterialTheme.typography.labelLarge,
                        color = color,
                        fontSize = 25.sp
                    )
                }
            }
        }
    }

    @Composable
    fun AirQualityBar(
        thresholdValues: AirQualityValues,
        value: String,
        isLoading: Boolean = false
    ) {
        if (isLoading) {

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(Colors.AQI_BAR.color, RoundedCornerShape(10.dp))
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(10.dp)),
                    color = Colors.AQI_BAR.color
                )
            }

        } else {
            val numericValue = value.toDoubleOrNull() ?: 0.0
            val boundedNumericValue =
                numericValue.coerceIn(thresholdValues.minValue, thresholdValues.maxValue)

            val level = toRangeStatus(boundedNumericValue, thresholdValues)

            val color = level.toColor().color

            val fillRatio = calculateFillRatio(
                boundedNumericValue,
                thresholdValues.maxValue,
                thresholdValues.minValue
            )

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(Colors.AQI_BAR.color, RoundedCornerShape(10.dp))
            ) {
                if (fillRatio > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(
                                fillRatio
                                    .toFloat()
                                    .coerceIn(0.0F, 1.0F)
                            )
                            .background(color = color, shape = RoundedCornerShape(10.dp))
                    )
                }
            }
        }
    }

    private fun calculateFillRatio(
        boundedNumericValue: Double,
        maxValue: Double,
        lowestValue: Double
    ): Double {

        return if (maxValue > lowestValue) {

            (boundedNumericValue - lowestValue) / (maxValue - lowestValue)

        } else 0.0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun AirQualityCardsRow(
        listOfStreams: List<Stream>,
        axonDetailViewModel: AxonDetailViewModel
    ) {
        Column(modifier = Modifier.padding(8.dp, 20.dp)) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                listOfStreams.forEach { stream ->
                    if (stream.type == StreamType.EAQI) return@forEach
                    StreamItem(stream = stream, axonDetailViewModel = axonDetailViewModel)
                }
            }
        }
    }

    private fun toRangeStatus(value: Double, airQualityValues: AirQualityValues): String {
        return when {
            value > airQualityValues.veryUnhealthy -> "Hazardous"
            value > airQualityValues.unhealthy -> "Very Unhealthy"
            value > airQualityValues.unhealthyFSG -> "Unhealthy"
            value > airQualityValues.moderate -> "Unhealthy for Sensitive Groups"
            value > airQualityValues.good -> "Moderate"
            else -> "Good"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseTime(input: String, patternInput: String, patternOutput: String): String {
        val formatter = DateTimeFormatter.ofPattern(patternOutput)
        val dateTime = LocalDateTime.parse(input, formatter)
        return dateTime.format(DateTimeFormatter.ofPattern(patternInput))
    }