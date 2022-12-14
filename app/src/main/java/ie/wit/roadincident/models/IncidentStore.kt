package ie.wit.roadincident.models

import android.widget.EditText

interface IncidentStore  {
    fun findAll(): List<IncidentModel>
    fun create(incident: IncidentModel)
    fun update(incident: IncidentModel)
    fun delete(incident: IncidentModel)
}