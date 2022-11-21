package ie.wit.roadincident.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityIncidentBinding
import ie.wit.roadincident.main.MainApp
import ie.wit.roadincident.models.IncidentModel
import timber.log.Timber
import timber.log.Timber.i

class IncidentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidentBinding
    var incident = IncidentModel()
    //val placemarks = ArrayList<PlacemarkModel>()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Incident Activity started...")

        if (intent.hasExtra("incident_edit")) {
            incident = intent.extras?.getParcelable("incident_edit")!!
            binding.incidentTitle.setText(incident.title)
            binding.description.setText(incident.description)
        }

        binding.btnAdd.setOnClickListener() {
            incident.title = binding.incidentTitle.text.toString()
            incident.description = binding.description.text.toString()
            if (incident.title.isNotEmpty()) {
                app.incidents.create(incident.copy())
                i("add Button Pressed: ${incident}")
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_incident, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

