package com.pnhung.icarchecking.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.Storage
import com.pnhung.icarchecking.view.api.model.CarInforModelRes
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity

class MapManager private constructor() : LocationCallback() {
    var mMap: GoogleMap? = null
    lateinit var mContext: Context
    private var myLocation: Marker? = null
    private lateinit var fusedLPC: FusedLocationProviderClient
    private val listCarMarker: ArrayList<Marker> = ArrayList()

    @SuppressLint("VisibleForTests")
    fun initMap() {
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.setAllGesturesEnabled(true)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.setInfoWindowAdapter(initAdapter())

        //update my location
        fusedLPC = FusedLocationProviderClient(mContext)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        fusedLPC.requestLocationUpdates(locationRequest, this, Looper.getMainLooper())
    }


    private fun initAdapter(): GoogleMap.InfoWindowAdapter {
        return object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(p0: Marker): View? {
                return initCarInfoView(p0)
            }
            override fun getInfoContents(p0: Marker): View? {
                return initCarInfoView(p0)
            }

        }
    }

    private fun initCarInfoView(marker: Marker): View? {
        val carInfo = marker.tag as CarInfoEntity
        val v = View.inflate(mContext, R.layout.item_info, null)
        return v
    }

    private fun updateMyLocation(result: LocationResult) {
        if (Storage.getInstance().myPosition == null) {
            Storage.getInstance().myPosition = result.locations[0]
            if (mMap == null) {
                return
            }
            showMyLocation()
        } else {
            Storage.getInstance().myPosition = result.locations[0]
        }
        Log.d(TAG, "updateMyLocation")
    }

    override fun onLocationResult(result: LocationResult) {
        ProgressLoading.dismiss()
        updateMyLocation(result)
    }

    fun showMyLocation() {
        val pos = LatLng(
            Storage.getInstance().myPosition!!.latitude,
            Storage.getInstance().myPosition!!.longitude
        )

        if (myLocation == null) {
            val myLocationOption = MarkerOptions()
            myLocationOption?.title("Vị trí của tôi")
            myLocationOption?.position(pos)
            myLocationOption?.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            myLocation = mMap?.addMarker(myLocationOption)
        } else {
            myLocation?.position = pos
        }

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
    }


    fun showListCar(carInforModelRes: CarInforModelRes) {
        if (carInforModelRes.data == null) {
            return
        }
        for (car in listCarMarker) {
            car.remove()
        }

        listCarMarker.clear()
        for ((index, car) in (carInforModelRes.data!!).withIndex()) {
            showCarOnMap(car, index)
        }
    }

    private fun showCarOnMap(car: CarInfoEntity, index: Int) {
        val option = MarkerOptions()
        option.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        option.position(pos)
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor))
        val marker = mMap?.addMarker(option)
        marker?.tag = car
        listCarMarker.add(marker!!)
        if (index == 0) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
        }
    }

    fun stopHandleLocation() {
        fusedLPC.removeLocationUpdates(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: MapManager? = null
        val TAG = MapManager::class.java.name

        fun getInstance(): MapManager {
            if (INSTANCE == null) {
                INSTANCE = MapManager()
            }
            return INSTANCE!!
        }
    }
}