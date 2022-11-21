package ie.wit.roadincident.models

interface IncidentStore {
    fun findAll(): List<IncidentModel>
    fun create(incident: IncidentModel)
    fun update(incident: IncidentModel)
}