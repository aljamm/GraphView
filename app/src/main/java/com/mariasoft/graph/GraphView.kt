package com.mariasoft.graph

import android.content.Context
import android.graphics.Canvas
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class GraphView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    val textViewTitle: TextView
    val textViewYAxis: TextView
    val textViewXAxis: TextView

    private val textViewYLabel01: TextView
    private val textViewYLabel02: TextView
    private val textViewYLabel03: TextView
    private val textViewYLabel04: TextView
    private val textViewYLabel05: TextView
    private val textViewYLabel06: TextView
    private val textViewYLabel07: TextView
    private val textViewYLabel08: TextView

    private val textViewXLabel01: TextView
    private val textViewXLabel02: TextView
    private val textViewXLabel03: TextView
    private val textViewXLabel04: TextView
    private val textViewXLabel05: TextView
    private val textViewXLabel06: TextView
    private val textViewXLabel07: TextView
    private val textViewXLabel08: TextView

    val lineGraph: LineGraph

    init {
//        isSaveEnabled = true
        inflate(context, R.layout.graph_view_layout, this)
        textViewTitle = findViewById(R.id.textViewGraphTitle)
        textViewYAxis = findViewById(R.id.textViewYAxisLabel)
        textViewXAxis = findViewById(R.id.textViewXAxisLabel)
        lineGraph = findViewById(R.id.lineGraph)

        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.GraphView)
        textViewTitle.text = attrs.getText(R.styleable.GraphView_title) ?: ""
        textViewYAxis.text = "Road and Pump Load (LIP)"
        textViewXAxis.text =  "Road and Pump Load (inch)"
        attrs.recycle()

        textViewYLabel01 = findViewById(R.id.textViewYLabel01)
        textViewYLabel02 = findViewById(R.id.textViewYLabel02)
        textViewYLabel03 = findViewById(R.id.textViewYLabel03)
        textViewYLabel04 = findViewById(R.id.textViewYLabel04)
        textViewYLabel05 = findViewById(R.id.textViewYLabel05)
        textViewYLabel06 = findViewById(R.id.textViewYLabel06)
        textViewYLabel07 = findViewById(R.id.textViewYLabel07)
        textViewYLabel08 = findViewById(R.id.textViewYLabel08)

        textViewXLabel01 = findViewById(R.id.textViewXLabel01)
        textViewXLabel02 = findViewById(R.id.textViewXLabel02)
        textViewXLabel03 = findViewById(R.id.textViewXLabel03)
        textViewXLabel04 = findViewById(R.id.textViewXLabel04)
        textViewXLabel05 = findViewById(R.id.textViewXLabel05)
        textViewXLabel06 = findViewById(R.id.textViewXLabel06)
        textViewXLabel07 = findViewById(R.id.textViewXLabel07)
        textViewXLabel08 = findViewById(R.id.textViewXLabel08)

    }
    fun addToPrimaryDataSet(dataSets: List<DataSet>) {
       lineGraph.primaryDataSets.addAll(dataSets)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        setLabels()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLabels()

    }

    private fun setLabels() {
        val yLabelValues = lineGraph.yAxisLabels()
        textViewYLabel01.text = yLabelValues[0]
        textViewYLabel02.text = yLabelValues[1]
        textViewYLabel03.text = yLabelValues[2]
        textViewYLabel04.text = yLabelValues[3]
        textViewYLabel05.text = yLabelValues[4]
        textViewYLabel06.text = yLabelValues[5]
        textViewYLabel07.text = yLabelValues[6]
        textViewYLabel08.text = yLabelValues[7]

        val xLabelValues = lineGraph.xAxisLabels()
        textViewXLabel01.text = xLabelValues[0]
        textViewXLabel02.text = xLabelValues[1]
        textViewXLabel03.text = xLabelValues[2]
        textViewXLabel04.text = xLabelValues[3]
        textViewXLabel05.text = xLabelValues[4]
        textViewXLabel06.text = xLabelValues[5]
        textViewXLabel07.text = xLabelValues[6]
        textViewXLabel08.text = xLabelValues[7]
    }


}