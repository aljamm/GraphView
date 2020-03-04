package com.mariasoft.graph

import android.graphics.Paint


class DataSet(val xValues: Array<Long>, val yValues: Array<Long>, val paint: Paint, val graphType: GraphType = GraphType.STANDARD_LINE) {

    enum class GraphType {
        CYCLIC_LINE,
        STANDARD_LINE,
        CONSTANT_LINE,
        STATE_LINE
    }
}