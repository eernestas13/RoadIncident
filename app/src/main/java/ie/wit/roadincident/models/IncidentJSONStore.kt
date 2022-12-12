package ie.wit.roadincident.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.roadincident.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "incidents.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<IncidentModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class IncidentJSONStore(private val context: Context) : IncidentStore {

    var incidents = mutableListOf<IncidentModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<IncidentModel> {
        logAll()
        return incidents
    }

    override fun create(incident: IncidentModel) {
        incident.id = generateRandomId()
        incidents.add(incident)
        serialize()
    }


    override fun update(incident: IncidentModel) {
        val incidentsList = findAll() as ArrayList<IncidentModel>
        var foundIncident: IncidentModel? = incidentsList.find { p -> p.id == incident.id }
        if (foundIncident != null) {
            foundIncident.title = incident.title
            foundIncident.description = incident.description
            foundIncident.image = incident.image
            foundIncident.lat = incident.lat
            foundIncident.lng = incident.lng
            foundIncident.zoom = incident.zoom
        }
        serialize()
    }

    override fun delete(incident: IncidentModel) {
        incidents.remove(incident)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(incidents, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        incidents = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        incidents.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}