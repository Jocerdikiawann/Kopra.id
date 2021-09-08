package com.trian.component.chart

import android.annotation.SuppressLint
import android.graphics.LinearGradient

import android.graphics.Paint
import android.graphics.Shader
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.marginBottom
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.trian.component.R
import com.trian.component.ui.theme.ColorBackground
import com.trian.component.ui.theme.ColorFontSw
import com.trian.component.utils.CustomChartMarker
import com.trian.component.utils.coloredShadow

/**
 * Base Chart
 * Author PT Cexup Telemedhicine
 * Created by Trian Damai
 * 07/09/2021
 */
@SuppressLint("ResourceAsColor")
@Composable
fun BaseChartView(list:ArrayList<Entry>){
    AndroidView(
        modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent),
        factory = {
           LineChart(ContextThemeWrapper(it,R.style.Chart))

        },
        update = {
            view->


            view.setBackgroundResource(R.drawable.bg_chart)

            //disable axis
            view.xAxis.setDrawGridLines(false)
            view.xAxis.position = XAxis.XAxisPosition.BOTTOM


            view.axisRight.isEnabled = false
            view.axisRight.setDrawAxisLine(false)
            view.axisRight.setDrawGridLines(false)

            view.axisLeft.isEnabled = false
            view.axisLeft.setDrawAxisLine(false)
            view.axisLeft.setDrawGridLines(false)

            view.legend.isEnabled = false

            //set gradient line


            //Part3
            val vl = LineDataSet(list, "My Type")
            //make chart smooth
            vl.mode = LineDataSet.Mode.CUBIC_BEZIER
            vl.cubicIntensity = 0.2f
            //set transparency
            vl.fillAlpha = 20

            //
            vl.addColor(R.color.yellow)
            //set value in each circle
            vl.setDrawValues(true)
            //Part4 set color fill (area)
            vl.setDrawFilled(true)
            vl.fillColor = R.color.text_blue
            vl.lineWidth = 3f



            //remove circle
            vl.setDrawCircles(true)

            //Part5
            view.xAxis.labelRotationAngle = 0f

            //Part6
            view.data = LineData(vl)

            //Part7
            view.axisRight.isEnabled = false
            view.xAxis.axisMaximum = 20+0.1f

            //Part8
            view.setTouchEnabled(true)
            view.setPinchZoom(true)

            //Part9
            view.description.text = "Days"
            view.setNoDataText("No Data to be shown!")

            //Part10
            view.animateX(1800, Easing.EaseInExpo)


            //add custom marker
            val markerView = CustomChartMarker(view.context,R.layout.layout_marker_chart)
            view.marker = markerView
            //
            view.invalidate()
        }
    )
}

@Preview
@Composable
fun PreviewBaseChartView(){
    BaseChartView(list = arrayListOf<Entry>())
}