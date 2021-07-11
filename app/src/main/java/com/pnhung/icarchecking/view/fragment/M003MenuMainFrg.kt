package com.pnhung.icarchecking.view.fragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.SupportMapFragment
import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.Storage
import com.pnhung.icarchecking.databinding.FrgM003MenuMainBinding
import com.pnhung.icarchecking.view.MapManager
import com.pnhung.icarchecking.view.ProgressLoading
import com.pnhung.icarchecking.view.api.model.CarInforModelRes
import com.pnhung.icarchecking.view.api.model.UserInfoModelRes
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.dialog.CarInfoDialog
import com.pnhung.icarchecking.view.dialog.UpdateUserNameDialog
import com.pnhung.icarchecking.view.dialog.UserInfoDialog
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import com.pnhung.icarchecking.view.viewmodel.M003MenuMainViewModel
import com.pnhung.icarchecking.view.viewmodel.UpdateUserNameViewModel

class M003MenuMainFrg : BaseFragment<FrgM003MenuMainBinding, M003MenuMainViewModel>() {
    private var userInfoDialog: UserInfoDialog? = null
    override fun initViews() {
        checkPermission()
        val userName = CommonUtils.getInstance().getPref(BaseViewModel.USER_NAME)
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)

        binding?.includeMenu?.tvPhone?.text = phone
        binding?.includeMenu?.tvUserName?.text = userName ?: "Chưa cập nhật tên"

        binding?.drawer?.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                binding?.includeActionBar?.ivMenu?.setImageResource(R.drawable.ic_menu_open_24)
            }

            override fun onDrawerClosed(drawerView: View) {
                binding?.includeActionBar?.ivMenu?.setImageResource(R.drawable.ic_menu_24)
            }
        })

        binding?.includeActionBar?.ivMenu?.setOnClickListener(this)
        binding?.includeActionBar?.ivMyLocation?.setOnClickListener(this)
        binding?.includeActionBar?.ivCarList?.setOnClickListener(this)
        binding?.includeMenu?.tvLogOut?.setOnClickListener(this)
        binding?.includeMenu?.tbrMyLocation?.setOnClickListener(this)
        binding?.includeMenu?.tbrUserInfo?.setOnClickListener(this)

        MapManager.getInstance().callBack = this
        val mapFragment = childFragmentManager.findFragmentById(R.id.frg_map) as SupportMapFragment
        mapFragment.getMapAsync {
            ProgressLoading.show(mContext, false)
            MapManager.getInstance().initMap(it)
        }
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_menu -> {
                if (binding?.drawer?.isDrawerOpen(GravityCompat.START)!!) {
                    binding?.drawer?.closeDrawers()
                } else {
                    binding?.drawer?.openDrawer(GravityCompat.START)
                }
            }
            R.id.iv_my_location, R.id.tbr_my_location -> {
                MapManager.getInstance().showMyLocation()
                binding?.drawer?.closeDrawers()
            }

            R.id.iv_car_list -> {
                mViewModel.getListCar()
            }

            R.id.tv_log_out -> {
                logout()
            }
            R.id.tbr_user_info -> {
                ProgressLoading.show(mContext, false)
                onClickTbrUserInfo()
            }
        }
    }

    private fun onClickTbrUserInfo() {
        if (userInfoDialog == null) {
            userInfoDialog = UserInfoDialog(mContext, this)
        }
        ProgressLoading.dismiss()
        userInfoDialog?.show()
    }

    override fun logout() {
        CommonUtils.getInstance().clearPref(BaseViewModel.USER_NAME)
        CommonUtils.getInstance().clearPref(BaseViewModel.PHONE)
        CommonUtils.getInstance().clearPref(BaseViewModel.TOKEN)
        Storage.getInstance().clearAll()
        MapManager.getInstance().stopHandleLocation()
        callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
    }

    override fun callBack(key: String, data: Any?) {
        super.callBack(key, data)
        when (key) {
            M003MenuMainViewModel.API_GET_LIST_CAR -> {
                showListCarOnMap(data)
            }
            UpdateUserNameViewModel.API_KEY_UPDATE_USERNAME -> {
                updateUserName(data)
            }
            CarInfoDialog.KEY_SHOW_TRACKING -> {
                callBack?.showFrg(TAG, data, M007TrackingFrg.TAG, true)
            }
            CarInfoDialog.KEY_SHOW_TIME_LINE -> {
                callBack?.showFrg(TAG, data, M004TimeLineFrg.TAG, true)
            }
        }
    }

    private fun updateUserName(data: Any?) {
        if (data != null) {
            binding?.drawer?.closeDrawers()
            val updateUserModelRes = data as UserInfoModelRes
            Toast.makeText(mContext, "Cập nhật tên thành công!", Toast.LENGTH_SHORT).show()
            CommonUtils.getInstance()
                .savePref(BaseViewModel.USER_NAME, updateUserModelRes.data?.username)
            binding?.includeMenu?.tvUserName?.text = updateUserModelRes.data?.username
        }
    }

    private fun showListCarOnMap(data: Any?) {
        if (data != null) {
            MapManager.getInstance().showListCar(data as CarInforModelRes)
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (rs in grantResults) {
            if (rs != PackageManager.PERMISSION_GRANTED) {
                showNotify("Chưa được cấp quyền để truy cập google map")
            }
        }
    }

    private fun showNotify(text: String) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun initBinding(mRootView: View): FrgM003MenuMainBinding? {
        return FrgM003MenuMainBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M003MenuMainViewModel> {
        return M003MenuMainViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m003_menu_main
    }

    companion object {
        val TAG: String = M003MenuMainFrg::class.java.name
    }
}