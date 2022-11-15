package ie.wit.roadincident

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ie.wit.roadincident.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.incidentButton.setOnClickListener {
            val incidentText = getString(R.string.incident_text)
            Toast.makeText(applicationContext, incidentText, Toast.LENGTH_LONG).show()
        }
    }
}