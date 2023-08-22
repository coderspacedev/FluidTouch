package com.fluidsimulation

import android.app.*

class App : Application() {

   companion object{
       var app: App? = null
   }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}