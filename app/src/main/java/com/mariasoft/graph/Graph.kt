package com.mariasoft.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

abstract class Graph(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {

    val primaryDataSets: MutableList<DataSet> = mutableListOf()
    fun addToPrimaryDataSet(dataSets: List<DataSet>) {
        primaryDataSets.addAll(dataSets)
        invalidate()
    }

    protected val secondaryDataSets: MutableList<DataSet> = mutableListOf()
    fun addToSecondartDataSet(dataSets: List<DataSet>) {
        secondaryDataSets.addAll(dataSets)
        invalidate()
    }

    protected var dataSetMinX = Long.MAX_VALUE
    protected var dataSetMaxX = Long.MIN_VALUE
    protected var dataSetMinY = Long.MAX_VALUE
    protected var dataSetMaxY = Long.MIN_VALUE
    protected fun rangeOfXValues() = dataSetMaxX - dataSetMinX
    protected fun rangeOfYValues() = dataSetMaxY - dataSetMinY
    abstract fun calculateMinMax(dataSets: List<DataSet>)

    abstract fun xAxisLabels(): List<String>
    abstract fun yAxisLabels(): List<String>
    abstract fun secondaryAxisLabels(): List<String>

    protected var shouldDrawBorder: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    private fun drawBorder(canvas: Canvas?) {
        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 8f
        }
        val canvasHeight = canvas?.height?.toFloat() ?: 0f
        val canvasWidth = canvas?.width?.toFloat() ?: 0f
        canvas?.drawLine(0f, 0f, 0f, canvasHeight, paint)
        canvas?.drawLine(0f, canvasHeight, canvasWidth, canvasHeight, paint)
        canvas?.drawLine(0f, 0f, canvasWidth, 0f, paint)
        canvas?.drawLine(canvasWidth, 0f, canvasWidth, canvasHeight, paint)
    }

    override fun onDraw(canvas: Canvas?) {
        if (shouldDrawBorder) drawBorder(canvas)
    }
}