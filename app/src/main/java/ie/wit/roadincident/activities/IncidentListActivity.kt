package ie.wit.roadincident.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityIncidentListBinding
import ie.wit.roadincident.databinding.ActivityIncidentBinding
import ie.wit.roadincident.databinding.CardIncidentBinding
import ie.wit.roadincident.main.MainApp
import ie.wit.roadincident.models.IncidentModel

class IncidentListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityIncidentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncidentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = IncidentAdapter(app.incidents)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, IncidentActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.incidents.size)
            }
        }
}

class IncidentAdapter constructor(private var incidents: List<IncidentModel>) :
    RecyclerView.Adapter<IncidentAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardIncidentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val incident = incidents[holder.adapterPosition]
        holder.bind(incident)
    }

    override fun getItemCount(): Int = incidents.size

    class MainHolder(private val binding : CardIncidentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: IncidentModel) {
            binding.incidentTitle.text = incident.title
            binding.description.text = incident.description
        }
    }
}