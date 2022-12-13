package ie.wit.roadincident.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.roadincident.databinding.CardIncidentBinding
import ie.wit.roadincident.models.IncidentModel
import ie.wit.roadincident.databinding.ActivityIncidentBinding

private lateinit var binding2: ActivityIncidentBinding

interface IncidentListener {

    fun onIncidentClick(incident: IncidentModel)
}

class IncidentAdapter constructor(private var incidents: List<IncidentModel>,
                                   private val listener: IncidentListener) :
    RecyclerView.Adapter<IncidentAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardIncidentBinding

            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val incident = incidents[holder.adapterPosition]
        holder.bind(incident, listener)
    }

    override fun getItemCount(): Int = incidents.size

    class MainHolder(private val binding : CardIncidentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: IncidentModel, listener: IncidentListener) {

            binding.incidentTitle.text = incident.title
            binding.description.text = incident.description
            binding.numOfVehicles.text = incident.numVehicles
            Picasso.get().load(incident.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onIncidentClick(incident) }
        }
    }
}
