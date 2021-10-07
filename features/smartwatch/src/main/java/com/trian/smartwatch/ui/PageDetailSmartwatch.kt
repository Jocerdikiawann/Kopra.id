package com.trian.smartwatch.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.mikephil.charting.data.Entry
import com.trian.common.utils.route.Routes
import com.trian.common.utils.utils.*
import com.trian.component.appbar.AppBarFeature
import com.trian.component.chart.BaseChartView
import com.trian.component.chart.EcgView
import com.trian.component.ui.theme.BluePrimary
import com.trian.component.ui.theme.ColorFontFeatures
import com.trian.component.ui.theme.FontDeviceName
import com.trian.component.ui.theme.TesMultiModuleTheme
import com.trian.component.utils.DetailSmartwatchUI
import com.trian.data.utils.calculateMaxMin
import com.trian.data.utils.calculateSleepSummary
import com.trian.data.utils.explodeBloodPressure
import com.trian.data.viewmodel.SmartWatchViewModel
import com.trian.domain.models.bean.HistoryDatePickerModel
import compose.icons.Octicons
import compose.icons.octicons.Calendar24
import kotlinx.coroutines.CoroutineScope
import okhttp3.Route

@Composable
fun PageDetailSmartwatch(
    modifier:Modifier=Modifier,
    viewModel: SmartWatchViewModel,
    nav:NavHostController,
    scope:CoroutineScope,
    page:String,
    changeStatusBar:(Color)->Unit,
    onClickCalender: ()-> Unit
){



    val data = mutableListOf<Entry>()
    val data2 = mutableListOf<Entry>()
    var maxAxis by remember{ mutableStateOf(0f)}
    var minAxis by remember{ mutableStateOf(0f)}
    var date by viewModel.currentDate
    var latest by remember { mutableStateOf("0") }
    var max by remember { mutableStateOf("0") }
    var min by remember { mutableStateOf("0") }
    var sleepDuration by remember { mutableStateOf("0.0") }
    var fallSleep by remember { mutableStateOf("00:00") }
    var wakeTime by remember { mutableStateOf("0") }
    var awakeTime by remember { mutableStateOf("00:00") }
    var satuan by remember { mutableStateOf("") }


    val scaffoldState = rememberScaffoldState()

    fun initializePage(){
        when(page){
            Routes.SmartwatchRoute.DETAIL_TEMPERATURE->{
                maxAxis = 50f
                minAxis = 10f
                satuan = "c"

                viewModel.getTemperatureHistory()
            }
            Routes.SmartwatchRoute.DETAIL_RESPIRATION->{
                maxAxis = 60f
                minAxis = 5f
                satuan = "times/minute"
                viewModel.getRespirationHistory()
            }
            Routes.SmartwatchRoute.DETAIL_HEART_RATE->{
                maxAxis = 200f
                minAxis = 20f
                satuan = "bpm"
                viewModel.getHeartRateHistory()
            }
            Routes.SmartwatchRoute.DETAIL_BLOOD_OXYGEN->{
                maxAxis = 160f
                minAxis = 30f
                satuan = "%"
                viewModel.getBloodOxygenHistory()
            }
            Routes.SmartwatchRoute.DETAIL_BLOOD_PRESSURE->{
                maxAxis = 200f
                minAxis = 30f
                satuan = "mmHg"
                viewModel.getBloodPressureHistory()
            }
            Routes.SmartwatchRoute.DETAIL_ECG->{

            }
            Routes.SmartwatchRoute.DETAIL_SLEEP->{
                viewModel.getSleepHistory()
            }

        }
    }
    initializePage()
    //when the component do `Recompose`
    SideEffect {

    }
    //equivalent `onStart`,`onResume`
    LaunchedEffect(key1 = scaffoldState){
        changeStatusBar(Color.White)
        viewModel.changeCurrentDate(getLastDayTimeStamp(), getTodayTimeStamp())



    }

    //equivalent `onDestroy`
    DisposableEffect(key1 = scaffoldState){
        onDispose {
            viewModel.changeCurrentDate(getLastDayTimeStamp(), getTodayTimeStamp())
            when(page) {
                Routes.SmartwatchRoute.DETAIL_ECG ->{}

            }
        }
    }

    //calculate min max
    when(page){
        Routes.SmartwatchRoute.DETAIL_TEMPERATURE-> {
            val result by  viewModel.listTemperature
            result.forEachIndexed {
                    index, measurement ->
                data.add(
                    Entry(
                        index.toFloat(),
                         measurement.value.toFloat()

                    )
                )
            }
            result.calculateMaxMin{
                empty, lat, x, n ->
                if(!empty){
                    latest = "${lat!!.value}"
                    max = "${x!!.value}"
                    min = "${n!!.value}"
                }
            }
        }
        Routes.SmartwatchRoute.DETAIL_HEART_RATE-> {
            val result by  viewModel.listHeartRate
            result.forEachIndexed {
                    index, measurement ->
                data.add(
                    Entry(
                        index.toFloat(),
                        measurement.value.toFloat()

                    )
                )

            }
            result.calculateMaxMin{
                    empty, lat, x, n ->
                if(!empty){
                    latest = "${lat!!.value}"
                    max = "${x!!.value}"
                    min = "${n!!.value}"
                }
            }
        }
        Routes.SmartwatchRoute.DETAIL_RESPIRATION-> {
            val result by  viewModel.listRespiration
            result.forEachIndexed {
                    index, measurement ->
                data.add(
                    Entry(
                        index.toFloat(),
                        measurement.value.toFloat()

                    )
                )

            }
            result.calculateMaxMin{
                    empty, lat, x, n ->
                if(!empty){
                    latest = "${lat!!.value}"
                    max = "${x!!.value}"
                    min = "${n!!.value}"
                }
            }
        }
        Routes.SmartwatchRoute.DETAIL_BLOOD_OXYGEN-> {
            val result by  viewModel.listBloodOxygen
            result.forEachIndexed {
                    index, measurement ->
                data.add(
                    Entry(
                        index.toFloat(),
                        measurement.value.toFloat()

                    )
                )

            }
            result.calculateMaxMin{
                    empty, lat, x, n ->
                if(!empty){
                    latest = "${lat!!.value}"
                    max = "${x!!.value}"
                    min = "${n!!.value}"
                }
            }
        }
        Routes.SmartwatchRoute.DETAIL_BLOOD_PRESSURE-> {
            val result by  viewModel.listBloodPressure
            result.forEachIndexed {
                    index, measurement ->
                val bpm = measurement.value.explodeBloodPressure()
                data.add(
                    Entry(
                        index.toFloat(),
                        bpm.systole.toFloat()

                    )
                )
                data2.add(
                    Entry(
                        index.toFloat(),
                        bpm.diastole.toFloat()
                    )
                )

            }
            result.calculateMaxMin{
                    empty, lat, x, n ->
                if(!empty){
                    val bpmLatest = lat!!.value.explodeBloodPressure()
                    val bpmMax = x!!.value.explodeBloodPressure()
                    val bpmMin = n!!.value.explodeBloodPressure()
                    latest = "${bpmLatest.systole}/${bpmLatest.diastole}"
                    max = "${bpmMax.systole}/${bpmMax.diastole}"
                    min = "${bpmMin.systole}/${bpmMin.diastole}"
                }
            }
        }
        Routes.SmartwatchRoute.DETAIL_SLEEP-> {
            val result by  viewModel.listSleep
//            result.forEachIndexed {
//                    index, measurement ->
//                data.add(
//                    Entry(
//                        index.toFloat(),
//                        0f
//                    )
//                )
//
//            }
            if(result.isNotEmpty()){
                result[0].calculateSleepSummary { totalDuration, deepSleep, lightSleep, wake, fallSleepTime, awake ->
                    sleepDuration = totalDuration
                    wakeTime = wake
                    fallSleep = fallSleepTime
                    awakeTime = awake
                }
            }
        }
        else-> {

        }
    }
            DetailSmartwatchUI(
                appBar = {
                    AppBarFeature(name = "Andi", image ="" , onBackPressed = { /*TODO*/ }, onProfile = {})
                },
                scaffoldState = scaffoldState
            ){
                header {
                    when(page){
                        Routes.SmartwatchRoute.DETAIL_ECG ->{
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .background(Color.White),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "60", //diastole
                                        fontSize = 24.sp,
                                        color = ColorFontFeatures
                                    )
                                    Text(
                                        text = "bpm",
                                        fontSize = 16.sp,
                                        color = ColorFontFeatures,
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row() {
                                        Text(
                                            text = "60", //diastole
                                            fontSize = 24.sp,
                                            color = ColorFontFeatures
                                        )
                                        Text(
                                            text = "/",
                                            fontSize = 24.sp,
                                            color = ColorFontFeatures
                                        )
                                        Text(
                                            text = "60", //diastole
                                            fontSize = 24.sp,
                                            color = ColorFontFeatures
                                        )
                                    }

                                    Text(
                                        text = "mmHg",
                                        fontSize = 16.sp,
                                        color = ColorFontFeatures,
                                    )
                                }

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "60", //diastole
                                        fontSize = 24.sp,
                                        color = ColorFontFeatures
                                    )
                                    Text(
                                        text = "HRV",
                                        fontSize = 16.sp,
                                        color = ColorFontFeatures,
                                    )
                                }

                            }
                        }
                        else ->{

                            //latest value
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = latest, //diastole
                                    fontSize = 32.sp,
                                    color = ColorFontFeatures
                                )
                                Spacer(modifier = modifier.width(5.dp))
                                Text(
                                    text = satuan,
                                    fontSize = 16.sp,
                                    color = ColorFontFeatures,
                                    modifier = modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
                body {
                        when (page) {
                            Routes.SmartwatchRoute.DETAIL_ECG->{
                                val ecgwave by viewModel.ecgWave


                                Column(modifier=modifier.fillMaxHeight(0.8f)) {
                                    EcgView(list=ecgwave)
                                    //  EcgView(list=ecgwave)
                                }
                            }
                            Routes.SmartwatchRoute.DETAIL_BLOOD_PRESSURE -> {

                                Column(
                                    modifier = modifier
                                        .fillMaxHeight(0.4f)
                                        .background(Color.White)
                                        .padding(horizontal = 16.dp, vertical = 10.dp)

                                ) {
                                    BaseChartView(
                                        data = data,
                                        description = "Systole",
                                        maxAxis = 250f,
                                        minAxis = 70f
                                    )
                                }
                                Column(
                                    modifier = modifier
                                        .fillMaxHeight(0.5f)
                                        .background(Color.White)
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    BaseChartView(
                                        data = data2,
                                        description = "Diastole",
                                        maxAxis = 150f,
                                        minAxis = 50f
                                    )
                                }
                                ChooseCalender(
                                    date = date.from.formatReadableDate(),
                                    onNext = {

                                       val fromNext = date.from.getNextDate()
                                        date =   HistoryDatePickerModel(
                                                     from =fromNext,
                                                     to =  fromNext.getNextDate()
                                                 )


                                        initializePage()
                                    },
                                    onPrev = {

                                            val fromPrev = date.from.getPreviousDate()
                                        date =  HistoryDatePickerModel(
                                                from= fromPrev,
                                                to= fromPrev.getNextDate()
                                            )
                                        initializePage()

                                    },
                                    onClickCalender = {}
                                )
                            }
                            else ->{
                                Column(
                                    modifier = modifier
                                        .background(Color.White)
                                        .fillMaxHeight(0.7f)
                                        .padding(horizontal = 16.dp, vertical = 10.dp)

                                ) {
                                    BaseChartView(
                                        data = data,
                                        description = when(page){
                                            Routes.SmartwatchRoute.DETAIL_BLOOD_OXYGEN-> "SpO2"
                                            Routes.SmartwatchRoute.DETAIL_TEMPERATURE->"Temperature"
                                            Routes.SmartwatchRoute.DETAIL_HEART_RATE->"Heart Rate"
                                            Routes.SmartwatchRoute.DETAIL_RESPIRATION->"Respiration"
                                            else->""
                                        }, //deskripsi heartrate,temperature,SpO2,Respiratory
                                        maxAxis = maxAxis,
                                        minAxis = minAxis
                                    )
                                }
                                ChooseCalender(
                                    date = date.from.formatReadableDate(),
                                    onNext = {

                                        val fromNext = date.from.getNextDate()
                                        date =   HistoryDatePickerModel(
                                            from =fromNext,
                                            to =  fromNext.getNextDate()
                                        )
                                        initializePage()
                                    },
                                    onPrev = {

                                        val fromPrev = date.from.getPreviousDate()
                                        date =  HistoryDatePickerModel(
                                            from= fromPrev,
                                            to= fromPrev.getNextDate()
                                        )
                                        initializePage()

                                    },
                                    onClickCalender = {}
                                )
                            }
                        }
                }
                footer {
                    val durationSplit = sleepDuration.split(".")
                        when(page) {
                            Routes.SmartwatchRoute.DETAIL_SLEEP -> {
                                Column(
                                    modifier = modifier
                                        .background(FontDeviceName)
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Spacer(modifier = modifier.height(16.dp))
                                    Row(
                                        modifier = modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = durationSplit[0], //value hour of sleep duration
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "H",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                                Spacer(modifier = modifier.width(5.dp))
                                                Text(
                                                    text = durationSplit[1],//value minute of sleep duration
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "M",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                            }
                                            Text(
                                                text = "Sleep Duration",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = wakeTime, //value wake time
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                            }
                                            Text(
                                                text = "Wake Time",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }

                                    }
                                    Spacer(modifier = modifier.height(16.dp))
                                    Row(
                                        modifier = modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = fallSleep, //value time
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )

                                            }
                                            Text(
                                                text = "Fall Asleep Time",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = awakeTime, //value time Awake Time
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                            }
                                            Text(
                                                text = "Awake Time",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }

                                    }
                                    Spacer(modifier = modifier.height(16.dp))
                                    Row(
                                        modifier = modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = "00", //value hour of light sleep
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "H",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                                Spacer(modifier = modifier.width(5.dp))
                                                Text(
                                                    text = "00",//value minute of Light sleep
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "M",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                            }
                                            Text(
                                                text = "Light Sleep",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = "00", //value hour of deep sleep
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "H",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                                Spacer(modifier = modifier.width(5.dp))
                                                Text(
                                                    text = "00",//value minute of deep sleep
                                                    fontSize = 26.sp,
                                                    color = ColorFontFeatures
                                                )
                                                Spacer(modifier = modifier.width(2.dp))
                                                Text(
                                                    text= "M",
                                                    fontSize = 14.sp,
                                                    color = ColorFontFeatures,
                                                    modifier = modifier.padding(top = 10.dp)
                                                )
                                            }
                                            Text(
                                                text = "Deep Sleep",
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                            )
                                        }

                                    }
                                    Spacer(modifier = modifier.height(16.dp))
                                }
                            }
                            Routes.SmartwatchRoute.DETAIL_ECG ->{
                                var recordState by remember {
                                    mutableStateOf(false)
                                }
                                var progress by remember{ mutableStateOf(0.0f)}
                                val animatedProgress = animateFloatAsState(
                                    targetValue = progress,
                                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                                ).value
                                var persentage : Float = (progress/1f)*100
                                var persen = persentage.toInt()
                                var isStart by remember{
                                    mutableStateOf(false)
                                }
                                Row(
                                    modifier = modifier
                                        .background(FontDeviceName)
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = modifier
                                            .fillMaxHeight()
                                            .padding(horizontal = 16.dp)
                                    ) {

                                        Text(
                                            text = "$persen %",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Left,
                                            color = Color.White
                                        )
                                        LinearProgressIndicator(
                                            progress = animatedProgress,
                                            color = BluePrimary,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(10.dp)
                                                .clip(shape = RoundedCornerShape(5.dp))
                                        )
                                        Spacer(modifier = Modifier.height(30.dp))
                                        Button(
                                            onClick = {
                                                if(isStart){
                                                    isStart = false
                                                    viewModel.endEcg()
                                                }else {
                                                    isStart = true
                                                    viewModel.startEcgTest()
                                                }
                                                recordState = !recordState
                                                if(progress <1f) {
                                                    progress +=0.01f
                                                }

                                            },
                                            modifier = modifier
                                                .width(150.dp),
                                        ) {
                                            if(recordState){
                                                Icon(
                                                    Icons.Filled.Stop,
                                                    contentDescription = "play",
                                                    tint = Color.White,
                                                    modifier = modifier.size(24.dp)
                                                )
                                            }else{
                                                Icon(
                                                    Icons.Filled.PlayArrow,
                                                    contentDescription = "stop",
                                                    tint = Color.White,
                                                    modifier = modifier.size(24.dp)
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                            else -> {
                                Row(
                                    modifier = modifier
                                        .background(FontDeviceName)
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = modifier.fillMaxHeight()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = max, //value heartrate,temperature,SpO2,Respiratory
                                                fontSize = 26.sp,
                                                color = ColorFontFeatures
                                            )
                                            Spacer(modifier = modifier.width(5.dp))
                                            Text(
                                                text=satuan,
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                                modifier = modifier.padding(top = 10.dp)
                                            )
                                        }
                                        Text(
                                            text = "Max",
                                            fontSize = 14.sp,
                                            color = ColorFontFeatures,
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = modifier.fillMaxHeight()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = min, //value heartrate,temperature,SpO2,Respiratory
                                                fontSize = 26.sp,
                                                color = ColorFontFeatures
                                            )
                                            Spacer(modifier = modifier.width(5.dp))
                                            Text(
                                                text= satuan,//satuan heartrate,temperature,SpO2,Respiratory
                                                fontSize = 14.sp,
                                                color = ColorFontFeatures,
                                                modifier = modifier.padding(top = 10.dp)
                                            )
                                        }
                                        Text(
                                            text = "Min",
                                            fontSize = 14.sp,
                                            color = ColorFontFeatures,
                                        )
                                    }

                                }

                            }
                        }
                }
            }

}

@Composable
fun EcgUiTest(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .background(FontDeviceName)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            var recordState by remember {
                mutableStateOf(false)
            }
            var progress by remember{ mutableStateOf(0.0f)}
            val animatedProgress = animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            ).value
            var persentage : Float = (progress/1f)*100
            var persen = persentage.toInt()
            Text(
                text = "$persen %",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                color = Color.White
            )
            LinearProgressIndicator(
                progress = animatedProgress,
                color = BluePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(shape = RoundedCornerShape(5.dp))
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    recordState = !recordState
                    if(progress <1f) {
                        progress +=0.01f
                    }},
                modifier = modifier
                    .width(150.dp),
            ) {
                if(recordState){
                    Icon(
                        Icons.Filled.Stop,
                        contentDescription = "play",
                        tint = Color.White,
                        modifier = modifier.size(24.dp)
                    )
                }else{
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "stop",
                        tint = Color.White,
                        modifier = modifier.size(24.dp)
                    )
                }
            }

            }
        }


    }

@Composable
fun ChooseCalender(
    modifier: Modifier = Modifier,
    date:String="Mon,Sep 14",
    onClickCalender: () -> Unit,
    onNext:()->Unit,
    onPrev:()->Unit
){
    //calender
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconToggleButton(checked = false, onCheckedChange = {
            onPrev()
        }) {
            Icon(
                Icons.Filled.ArrowBackIos,
                contentDescription = "Previous Date",
                tint = ColorFontFeatures,
            )
        }
        Text(
            text = date,
            modifier = modifier
                .clickable { onClickCalender() },
            textAlign = TextAlign.Center,
            color = ColorFontFeatures,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        IconToggleButton(checked = false, onCheckedChange = {
            onNext()
        }) {
            Icon(
                Icons.Filled.ArrowForwardIos,
                contentDescription = "Next Date",
                tint = ColorFontFeatures,
            )
        }

    }
}

@Preview
@Composable
fun DetailSmartWatchUiPreview(){
    TesMultiModuleTheme {
//        DetailSmartWatchUi(
//            onClickCalender = {},
//            page="Bpm",
//            viewModel = viewModel(),
//            scope = rememberCoroutineScope(),
//            nav = rememberNavController()
//        )
//        EcgUiTest()
        ChooseCalender( date = "",
            onNext = {},
            onPrev = {},
            onClickCalender = {})
    }
}