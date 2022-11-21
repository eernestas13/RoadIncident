package ie.wit.roadincident.main

import android.app.Application
import ie.wit.roadincident.models.IncidentModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application(){

    val incidents = ArrayList<IncidentModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Incident started")
     //   incidents.add(IncidentModel("One", "About one..."))
     //   incidents.add(IncidentModel("Two", "About two..."))
     //   incidents.add(IncidentModel("Three", "About three..."))
    }
}