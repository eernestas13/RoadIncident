package ie.wit.roadincident.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityMainBinding
import ie.wit.roadincident.models.IncidentModel
import timber.log.Timber
import timber.log.Timber.i

class IncidentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var incident = IncidentModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.incidentButton.setOnClickListener {
            val incidentText = getString(R.string.incident_text)
            Toast.makeText(applicationContext, incidentText, Toast.LENGTH_LONG).show()
        }
        Timber.plant(Timber.DebugTree())
        i("Incident Activity started..")

        binding.btnAdd.setOnClickListener() {
            incident.title = binding.incidentTitle.text.toString()
            if (incident.title.isNotEmpty()) {
                i("add Button Pressed: $incident.title")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}