package com.magicfluids

import android.content.*
import com.fluidsimulation.helper.*

const val APP = "NUM_APP_RUNS"
const val LWP = "NUM_LWP_RUNS"
fun getTotalNumRuns(context: Context): Int {
    return getNumRuns(context, APP) + getNumRuns(context, LWP)
}

fun getNumAppRuns(context: Context): Int {
    return getNumRuns(context, APP)
}

fun getNumLwpRuns(context: Context): Int {
    return getNumRuns(context, LWP)
}

private fun getNumRuns(context: Context, str: String): Int {
    val tinyDB = TinyDB(context, "RunInfo")
    return tinyDB.getInt(str, 0)
}

private fun incrementNumRuns(context: Context, str: String): Int {
    val tinyDB = TinyDB(context, "RunInfo")
    val i = tinyDB.getInt(str, 0) + 1
    tinyDB.putInt(str, i)
    return i
}

fun newAppRun(context: Context) {
    if (incrementNumRuns(context, APP) == 1) {
        getNumRuns(context, LWP)
    }
}