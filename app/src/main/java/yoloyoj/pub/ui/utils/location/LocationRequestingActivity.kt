package yoloyoj.pub.ui.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@SuppressLint("Registered")
open class LocationRequestingFragment : Fragment() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val LISTEN_CURRENT_LOCATION = "lcl"
        private const val CURRENT_LOCATION = "cl"
    }

    private var onLocationPermissionAllowed: List<() -> Unit> = emptyList()
    private var onLocationPermissionNotAllowed: List<() -> Unit> = emptyList()

    var onCurrentLocationUpdated: (location: Location?) -> Unit = {}
        set(value) {
            field = value
            field(currentLocation)
        }
    var currentLocation: Location? = null
        set(value) {
            field = value
            onCurrentLocationUpdated(value)
        }

    var listenCurrentLocation: Boolean = false
        @SuppressLint("MissingPermission") // permission check in requestLocationPermission
        set(value) {
            if (value) {
                requestLocationPermission(
                    onLocationPermissionAllowed = {
                        val locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, MyLocationListener())
                    }
                )
            }
            field = value
        }

    fun requestLocationPermission(
        title: String = "Location Permission Needed",
        message: String = "This app needs the Location permission, please accept to use location functionality",
        onLocationPermissionAllowed: () -> Unit = {},
        onLocationPermissionNotAllowed: () -> Unit = {}
    ) {
        this.onLocationPermissionAllowed += onLocationPermissionAllowed
        this.onLocationPermissionNotAllowed += onLocationPermissionNotAllowed

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) { // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(context!!)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(
                        resources.getString(android.R.string.ok)
                    ) { _, _ ->
                        // Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .setNegativeButton(
                        resources.getString(android.R.string.no)
                    ) { _: DialogInterface, _: Int ->
                        this.onLocationPermissionNotAllowed.forEach { it() }
                    }
                    .setOnDismissListener {
                        this.onLocationPermissionNotAllowed.forEach { it() }
                    }
                    .create()
                    .show()
            } else { // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
        else
            this.onLocationPermissionAllowed.forEach { it() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(LISTEN_CURRENT_LOCATION, listenCurrentLocation)
        outState.putParcelable(CURRENT_LOCATION, currentLocation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listenCurrentLocation = savedInstanceState?.getBoolean(LISTEN_CURRENT_LOCATION, false)?: false
        currentLocation = savedInstanceState?.getParcelable(CURRENT_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        onLocationPermissionAllowed.forEach { it() }
                    }
                } else {
                    onLocationPermissionNotAllowed.forEach { it() }
                }
                return
            }
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

    }

}
