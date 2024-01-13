package agh.ms.smogometr_1

import agh.ms.smogometr_1.data.AppContainer
import agh.ms.smogometr_1.data.AppDataContainer
import android.app.Application

class SmogometrApplication : Application(){

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

    }
}