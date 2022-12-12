package ie.wit.roadincident.main

import android.app.Application
import ie.wit.roadincident.models.IncidentJSONStore
import ie.wit.roadincident.models.IncidentMemStore
import ie.wit.roadincident.models.IncidentModel
import ie.wit.roadincident.models.IncidentStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application(){

    lateinit var incidents : IncidentStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        incidents = IncidentJSONStore(applicationContext)
        i("Incident started")
    }
}