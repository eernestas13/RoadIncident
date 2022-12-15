package ie.wit.roadincident.activities

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.roadincident.R
import ie.wit.roadincident.databinding.ActivityIncidentMapsBinding
import ie.wit.roadincident.databinding.ContentMainBinding
import ie.wit.roadincident.main.MainApp
import ie.wit.roadincident.models.IncidentJSONStore
import ie.wit.roadincident.models.IncidentModel
import ie.wit.roadincident.databinding.FragmentFirstBinding
import ie.wit.roadincident.databinding.FragmentSecondBinding


class IncidentMapsActivity : AppCompatActivity(),GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityIncidentMapsBinding
    private lateinit var contentBinding: ContentMainBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp
    var picture = IncidentModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidentMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        contentBinding = ContentMainBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        app = application as MainApp
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()

            findViewById<Button>(R.id.backButton).setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        contentBinding.currentTitle.text = "Marker Info :"
        contentBinding.currentDescription.text = marker.title
        val desc = marker.tag as? String
        return false
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.incidents.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val titleM = MarkerOptions().title(it.title).position(loc)
            val descriptionM = MarkerOptions().title(it.description).position(loc)
            map.addMarker(titleM)?.tag = it.id
         //   map.addMarker(descriptionM)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }
}


