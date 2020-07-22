package yoloyoj.pub.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import yoloyoj.pub.R


class MapFragment : Fragment(), OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onStart() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        super.onStart()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val gmap = googleMap
        gmap.setMinZoomPreference(12f)
        val ny =
            LatLng(40.7143528, -74.0059731)
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny))
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }
}
