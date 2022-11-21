package ie.wit.roadincident.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
    //val incidents = ArrayList<IncidentModel>()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.plant(Timber.DebugTree())
        i("Incident Activity started..")

        binding.btnAdd.setOnClickListener() {
            incident.title = binding.incidentTitle.text.toString()
            incident.description = binding.description.text.toString()
            if (incident.title.isNotEmpty()) {

                app.incidents.add(incident.copy())
                i("add Button Pressed: $incident") //incident.title
                for (i in app.incidents.indices) {
                    i("Incident[$i]:${this.app.incidents[i]}")
                }
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
}