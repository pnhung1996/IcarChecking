package com.pnhung.icarchecking.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.Storage
import com.pnhung.icarchecking.view.api.model.CarInforModelRes
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.dialog.CarInfoDialog

class MapManager private constructor() : LocationCallback() {
    var mMap: GoogleMap? = null
    lateinit var mContext: Context
    private var myLocation: Marker? = null
    private lateinit var fusedLPC: FusedLocationProviderClient
    private val listCarMarker: ArrayList<Marker> = ArrayList()
    private var carInfoDialog: CarInfoDialog? = null
    lateinit var callBack: OnActionCallBack
    private var startMarker: Marker? = null
    private var endMarker: Marker? = null
    private var polyLine: Polyline? = null

    @SuppressLint("VisibleForTests")
    fun initMap(map: GoogleMap) {
        mMap = map
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


        mMap?.setOnMarkerClickListener {
            if (it.tag != null) {
                showCarInfor(it.tag as CarInfoEntity)
            }
            true
        }

        //update my location
        fusedLPC = FusedLocationProviderClient(mContext)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        fusedLPC.requestLocationUpdates(locationRequest, this, Looper.getMainLooper())
    }

    private fun showCarInfor(carInfoEntity: CarInfoEntity) {
        if (carInfoDialog == null) {
            carInfoDialog = CarInfoDialog(mContext, carInfoEntity, callBack)
        } else {
            carInfoDialog?.reload(carInfoEntity)
        }
        carInfoDialog?.show()
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
            myLocationOption.title("Vị trí của tôi")
            myLocationOption.position(pos)
            myLocationOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            myLocation = mMap?.addMarker(myLocationOption)
        } else {
            myLocation?.position = pos
        }

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
    }


    fun showListCar(carInfoModelRes: CarInforModelRes) {
        if (carInfoModelRes.data == null) {
            return
        }
        for (car in listCarMarker) {
            car.remove()
        }

        listCarMarker.clear()
        for ((index, car) in (carInfoModelRes.data!!).withIndex()) {
            showCarOnMap(car, index)
        }
    }

    private fun showCarOnMap(car: CarInfoEntity, index: Int) {
        val option = MarkerOptions()
        option.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        option.position(pos)
        option.snippet(car.carNumber)
        option.icon(
            if (car.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor)
        )
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

    fun updateStatusCar(carInfo: CarInfoEntity?) {
        if (carInfo == null) return
        for (item in listCarMarker) {
            val tag = item.tag ?: continue
            val carItem = tag as CarInfoEntity
            if (carInfo.id == carItem.id) {
                carInfo.phoneManager = carItem.phoneManager
                carInfo.passwordManager = carItem.passwordManager
                item.tag = carInfo

                item.title = carInfo.carNumber
                val pos =
                    LatLng((carInfo.lastLat ?: "0").toDouble(), (carInfo.lastLng ?: "0").toDouble())
                item.position = pos
                item.snippet = carInfo.carNumber
                item.setIcon(
                    if (carInfo.activeStatus.equals("online"))
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor_active)
                    else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor)
                )

                break
            }
        }
    }

    fun updateTrackingCar(carInfo: CarInfoEntity) {
        if (endMarker == null || endMarker!!.tag == null) return
        val carItem = endMarker!!.tag as CarInfoEntity
        carInfo.phoneManager = carItem.phoneManager
        carInfo.passwordManager = carItem.passwordManager
        endMarker!!.tag = carInfo

        endMarker!!.title = carInfo.carNumber
        val pos =
            LatLng((carInfo.lastLat ?: "0").toDouble(), (carInfo.lastLng ?: "0").toDouble())
        endMarker!!.position = pos
        endMarker!!.snippet = carInfo.carNumber
        endMarker!!.setIcon(
            if (carInfo.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor)
        )

        val polyLinePoints:List<LatLng> = polyLine!!.points
        polyLine!!.points = polyLinePoints
        polyLine!!.points.add(pos)
    }

    fun showCarTracking(car: CarInfoEntity) {
        if (startMarker != null) {
            startMarker!!.remove()
            endMarker!!.remove()
        }
        val option = MarkerOptions()
        option.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        option.position(pos)
        option.snippet(car.carNumber)
        option.icon(
            BitmapDescriptorFactory.fromResource(R.drawable.ic_flag_64)
        )
        mMap?.setOnMarkerClickListener {
            true
        }

        startMarker = mMap?.addMarker(option)
        startMarker!!.tag = car

        option.icon(
            if (car.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_monitor)
        )
        endMarker = mMap?.addMarker(option)
        endMarker!!.tag = car

        val polyOption = PolylineOptions()
        polyOption.color(Color.RED)
        polyOption.width(10f)
        polyOption.add()
        polyLine = mMap?.addPolyline(polyOption)

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
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