package com.mariasoft.graph

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.mariasoft.graph.DataSet.GraphType.*
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

class LineGraph(context: Context, attributeSet: AttributeSet?) : Graph(context, attributeSet) {

    override fun xAxisLabels(): List<String> {
        val output = mutableListOf<String>()
        var valuePerLabel = rangeOfXValues() / 9
//        val roundingFactor = Math.pow(10.toDouble(), (valuePerLabel.toString().length / 2).toDouble()).toLong()
        var labelValue = (dataSetMinX) // / roundingFactor) * roundingFactor + roundingFactor
        // if (labelValue > dataSetMinX) labelValue = (dataSetMinX / roundingFactor) * roundingFactor - roundingFactor
        output.add(labelValue.toString())
        for (index in 2..9) {
            labelValue += valuePerLabel
            output.add(labelValue.toString())
        }
        return output
    }

    override fun yAxisLabels(): List<String> {
        val output = mutableListOf<String>()
        var valuePerLabel = rangeOfYValues() / 9
        val roundingFactor = Math.pow(10.toDouble(), (valuePerLabel.toString().length / 2).toDouble()).toLong()
        valuePerLabel = (valuePerLabel / roundingFactor) * roundingFactor
        var labelValue = (dataSetMinY / roundingFactor) * roundingFactor + roundingFactor
        if (labelValue > dataSetMinY) labelValue = (dataSetMinY / roundingFactor) * roundingFactor - roundingFactor
        output.add(labelValue.toString())
        for (index in 2..9) {
            labelValue += valuePerLabel
            output.add(labelValue.toString())
        }
        return output
    }

    override fun secondaryAxisLabels(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun calculateMinMax(dataSets: List<DataSet>) {
        when {
            dataSets.isEmpty() -> return
        }
        dataSetMinX = Long.MAX_VALUE
        dataSetMaxX = Long.MIN_VALUE
        dataSetMinY = Long.MAX_VALUE
        dataSetMaxY = Long.MIN_VALUE
        // Calculate Min/Max values
        dataSets.forEach { dataSet ->
            if (dataSet.graphType == CYCLIC_LINE || dataSet.graphType == STANDARD_LINE) {
                dataSetMaxX = Math.max(dataSet.xValues.max() ?: Long.MIN_VALUE, dataSetMaxX)
                dataSetMinX = Math.min(dataSet.xValues.min() ?: Long.MAX_VALUE, dataSetMinX)
            }
            if (dataSet.graphType != STATE_LINE) {
                dataSetMaxY = Math.max(dataSet.yValues.max() ?: Long.MIN_VALUE, dataSetMaxY)
                dataSetMinY = Math.min(dataSet.yValues.min() ?: Long.MAX_VALUE, dataSetMinY)
            }
        }
        // Adjust for padding factor
        when {
            graphPaddingFactor > 0 -> {
                dataSetMinX -= Math.abs(dataSetMaxX * graphPaddingFactor / 100)
                dataSetMinY -= Math.abs(dataSetMaxY * graphPaddingFactor / 100)
                dataSetMaxX += Math.abs(dataSetMaxX * graphPaddingFactor / 100)
                dataSetMaxY += Math.abs(dataSetMaxY * graphPaddingFactor / 100)
            }
        }
    }

    var graphPaddingFactor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        // TODO figure out how to set these directly in the XML. Current method of setting
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.LineGraph)
        shouldDrawBorder = attrs.getBoolean(R.styleable.LineGraph_shouldDrawBorder, true)
        graphPaddingFactor = attrs.getInt(R.styleable.LineGraph_graphPaddingFactor, 0)
        attrs.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        drawDividers(canvas)
        drawPrimaryDataSets(canvas)
        super.onDraw(canvas)
    }

     fun drawDividers(canvas: Canvas?) {
        val paint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 2f
        }
        val canvasHeight = canvas?.height?.toFloat() ?: 0f
        val canvasWidth = canvas?.width?.toFloat() ?: 0f
        val sectionWidth = canvasWidth / 9
        val sectionHeight = canvasHeight / 9

        for (i in 1..8) {
            canvas?.drawLine(i * sectionWidth, 0f, i * sectionWidth, canvasHeight, paint)
            canvas?.drawLine(0f, i * sectionHeight, canvasWidth, i * sectionHeight, paint)
        }
    }

     fun drawPrimaryDataSets(canvas: Canvas?) {
        canvas?.run {
            calculateMinMax(primaryDataSets)
            primaryDataSets.forEach { dataSet ->
                when (dataSet.graphType) {
                    CYCLIC_LINE -> drawStandardLine(this, dataSet, dataSetMinX, dataSetMinY, rangeOfXValues(), rangeOfYValues())
                    STANDARD_LINE -> drawUnfoldedLine(canvas, dataSet, dataSetMinY, rangeOfYValues())
                    CONSTANT_LINE -> drawConstantLine(this, dataSet, dataSetMinY, rangeOfYValues())
                    STATE_LINE -> drawStateLine(canvas, dataSet)
                }
            }
        }

    }

    private fun drawStandardLine(canvas: Canvas, dataSet: DataSet, dataSetMinX: Long, dataSetMinY: Long, rangeOfXValues: Long, rangeOfYValues: Long) {
        val pixelsPerX = canvas.width.toFloat() / rangeOfXValues
        val pixelsPerY = canvas.height.toFloat() / rangeOfYValues

        for (index in 0 until dataSet.xValues.size - 1) {
            val startX = (dataSet.xValues[index] - dataSetMinX).toFloat() * pixelsPerX
            val stopX = (dataSet.xValues[index + 1] - dataSetMinX).toFloat() * pixelsPerX
            val startY = canvas.height.toFloat() - (dataSet.yValues[index] - dataSetMinY) * pixelsPerY
            val stopY = canvas.height.toFloat() - (dataSet.yValues[index + 1] - dataSetMinY) * pixelsPerY
            canvas.drawLine(startX, startY, stopX, stopY, dataSet.paint)
        }
    }

    private fun drawConstantLine(canvas: Canvas, dataSet: DataSet, dataSetMinY: Long, rangeOfYValues: Long) {
        val pixelsPerY = canvas.height.toFloat() / rangeOfYValues.toFloat()
        val startY = canvas.height.toFloat() - (dataSet.yValues[0] - dataSetMinY).toFloat() * pixelsPerY
        val stopY = canvas.height.toFloat() - (dataSet.yValues[0] - dataSetMinY).toFloat() * pixelsPerY
        val stopX = canvas.width.toFloat()
        canvas.drawLine(0F, startY, stopX, stopY, dataSet.paint)
    }

    private fun drawStateLine(canvas: Canvas, dataSet: DataSet) {
        val pixelsPerX = canvas.width.toFloat() / dataSet.yValues.size

        for (index in 0 until dataSet.yValues.size - 1) {
            val startX = index.toFloat() * pixelsPerX
            val stopX = (index + 1).toFloat() * pixelsPerX
            val startY = when (dataSet.yValues[index]) {
                0L -> canvas.height.toFloat() * .15F
                else -> canvas.height.toFloat() * .85F
            }
            val stopY = when (dataSet.yValues[index + 1]) {
                0L -> canvas.height.toFloat() * .15F
                else -> canvas.height.toFloat() * .85F
            }
            canvas.drawLine(startX, startY, stopX, stopY, dataSet.paint)
        }
    }

    private fun drawUnfoldedLine(canvas: Canvas, dataSet: DataSet, dataSetMinY: Long, rangeOfYValues: Long) {
        val pixelsPerX = canvas.width.toFloat() / (dataSet.yValues.size - 1)
        val pixelsPerY = canvas.height.toFloat() / rangeOfYValues

        for (index in 0 until dataSet.yValues.size - 1) {
            val startX = index.toFloat() * pixelsPerX
            val stopX = (index + 1).toFloat() * pixelsPerX
            val startY = canvas.height - (dataSet.yValues[index] - dataSetMinY) * pixelsPerY
            val stopY = canvas.height - (dataSet.yValues[index + 1] - dataSetMinY) * pixelsPerY
            canvas.drawLine(startX, startY, stopX, stopY, dataSet.paint)
        }
    }
}