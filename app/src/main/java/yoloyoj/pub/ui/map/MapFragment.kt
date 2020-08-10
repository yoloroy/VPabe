package yoloyoj.pub.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import yoloyoj.pub.R
import yoloyoj.pub.models.Event
import yoloyoj.pub.ui.event.EventData
import yoloyoj.pub.ui.event.list.EventsListViewModel
import yoloyoj.pub.ui.event.view.EventActivity
import yoloyoj.pub.ui.event.view.EventEditActivity


@Suppress("DEPRECATION")
open class MapFragment : Fragment(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private lateinit var eventsListViewModel: EventsListViewModel
    private lateinit var events: EventData

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: SupportMapFragment

    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private var mCurrLocationMarker: Marker? = null
    private lateinit var mLastLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        eventsListViewModel =
            ViewModelProviders.of(this).get(EventsListViewModel::class.java)
        events = eventsListViewModel.events

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onStart() {
        mapView = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapView.getMapAsync(this)

        addEvent.setOnClickListener {
            startActivity(Intent(context, EventEditActivity::class.java))
        }

        super.onStart()
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        checkLocationPermission()
        googleMap.isMyLocationEnabled = true

        events.observeForever { loadAdapter(events.value!!) }
    }

    private fun loadAdapter(events: List<Event>) {
        googleMap.clear()

        val map = mutableMapOf<String, Event>()
        events.forEach { event ->
            if (
                (event.latlng != null) and
                !event.place.isNullOrBlank()
            ) {
                map[event.id] = event
                event.apply {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(
                                with(latlng!!) {
                                    LatLng(latitude, longitude)
                                }
                            )
                            .title(id)
                    )
                    googleMap.setOnMarkerClickListener {
                        val currentEvent = map[it.title!!]!!

                        val intent = Intent(context, EventActivity::class.java)
                        intent.putExtra("eventid", currentEvent.id)
                        context!!.startActivity(intent)

                        true
                    }
                }
            }
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        mCurrLocationMarker?.remove()
        //Place current location marker
        val latLng =
            LatLng(
                location.latitude,
                location.longitude
            )
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        mCurrLocationMarker = googleMap.addMarker(markerOptions)
        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private fun checkLocationPermission() {
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
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            activity!!,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        googleMap.isMyLocationEnabled = true
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}
