package ie.wit.roadincident.activities


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.squareup.picasso.Picasso
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityIncidentBinding
import ie.wit.roadincident.helpers.showImagePicker
import ie.wit.roadincident.main.MainApp
import ie.wit.roadincident.models.IncidentModel
import ie.wit.roadincident.models.Location
import ie.wit.roadincident.models.IncidentJSONStore


import timber.log.Timber
import timber.log.Timber.i

class IncidentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidentBinding
    var incident = IncidentModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    val db = Firebase.firestore

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false


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
            binding.numOfVehicles.setText(incident.numVehicles)
            binding.btnAdd.setText(R.string.save_incident)
            Picasso.get()
                .load(incident.image)
                .into(binding.incidentImage)
            if (incident.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_incident_image)
            }

        }

        binding.btnAdd.setOnClickListener() {
            incident.title = binding.incidentTitle.text.toString()
            incident.description = binding.description.text.toString()
            incident.numVehicles = binding.numOfVehicles.text.toString()


            if (incident.title.isEmpty())
                     { Toast.makeText(
                        this@IncidentActivity,
                        "Failed Creation! Title is Necessary!",
                        Toast.LENGTH_SHORT
                    ).show()


            } else {
                if (edit) {
                    app.incidents.update(incident.copy())
                } else  {
                    app.incidents.create(incident.copy())
                }
            }
            i("add Button Pressed: $incident")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        binding.incidentLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (incident.zoom != 0f) {
                location.lat =  incident.lat
                location.lng = incident.lng
                location.zoom = incident.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.buttonUpload.setOnClickListener() {
            db.collection("Incidents")
                .add(incident)
                .addOnSuccessListener { Toast.makeText(
                    this@IncidentActivity,
                    "Incident Uploaded!",
                    Toast.LENGTH_SHORT
                ).show()
                }
                .addOnFailureListener { Toast.makeText(
                    this@IncidentActivity,
                    "Incident Failed to Upload!",
                    Toast.LENGTH_SHORT
                ).show()
                }

        }

        registerImagePickerCallback()
        registerMapCallback()


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
            R.id.item_delete -> {
                app.incidents.delete(incident)
                setResult(99)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            incident.image = image

                            Picasso.get()
                                .load(incident.image)
                                .into(binding.incidentImage)
                            binding.chooseImage.setText(R.string.change_incident_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            incident.lat = location.lat
                            incident.lng = location.lng
                            incident.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}

