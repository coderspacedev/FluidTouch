package com.fluidsimulation.listeners

interface CommonListener {

    fun onOpen(any: Any?, position: Int) {}
}

interface ColorPickListener{
    fun color(color:Int){}
}