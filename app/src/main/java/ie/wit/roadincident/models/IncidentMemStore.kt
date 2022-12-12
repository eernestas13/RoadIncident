package ie.wit.roadincident.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class IncidentMemStore : IncidentStore {

    val incidents = ArrayList<IncidentModel>()

    override fun findAll(): List<IncidentModel> {
        return incidents
    }

    override fun create(incident: IncidentModel) {
        incident.id = getId()
        incidents.add(incident)
        logAll()
    }

    override fun update(incident: IncidentModel) {
        var foundIncident: IncidentModel? = incidents.find { p -> p.id == incident.id }
        if (foundIncident != null) {
            foundIncident.title = incident.title
            foundIncident.description = incident.description
            foundIncident.image = incident.image
            foundIncident.lat = incident.lat
            foundIncident.lng = incident.lng
            foundIncident.zoom = incident.zoom
            logAll()
        }
    }

    private fun logAll() {
        incidents.forEach { i("$it") }
    }
}