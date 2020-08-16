package yoloyoj.pub.ui.utils.loacation.getter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient

class LocationGetterActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        const val RESULT = 1

        const val LAT = "lat"
        const val LNG = "lng"
        const val ADDRESS = "address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_getter)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions().position(it).title("click"))

            apiClient.getAddress(
                "${it.latitude},${it.longitude}",
                "AngRR8VfPWw993O1mVna5spb0xLIU4GF0TZDgDHc-21Z0xDC7C3mYZvi9GBY92c-"
            )!!.enqueue(object : Callback<Map<String, Any>?> {
                override fun onFailure(call: Call<Map<String, Any>?>, t: Throwable) {}

                override fun onResponse(call: Call<Map<String, Any>?>, response: Response<Map<String, Any>?>) {
                    val intent = Intent().apply {
                        putExtra(LAT, it.latitude)
                        putExtra(LNG, it.longitude)
                        putExtra(
                            ADDRESS,
                            (
                                (
                                    (
                                        (
                                            response.body()!!
                                            ["resourceSets"] as List<*>
                                            )
                                        [0] as Map<*, *>
                                        )
                                    ["resources"] as List<*>
                                    )
                                [0] as Map<*, *>
                                )
                            ["name"] as String
                        )
                    }

                    setResult(RESULT, intent)
                    finish()
                }
            })
        }

        checkLocationPermission()
        mMap.isMyLocationEnabled = true

        // Add a marker in Moscow and move the camera
        val moscow = LatLng(55.752, 37.615)
        mMap.addMarker(MarkerOptions().position(moscow).title("Moscow"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
    }

    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) { // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(applicationContext)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        // Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else { // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }
}
