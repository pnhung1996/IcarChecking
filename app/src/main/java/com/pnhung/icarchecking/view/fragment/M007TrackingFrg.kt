package com.pnhung.icarchecking.view.fragment

import android.view.View
import com.google.android.gms.maps.SupportMapFragment
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.FrgM007TrackingBinding
import com.pnhung.icarchecking.view.MapManager
import com.pnhung.icarchecking.view.ProgressLoading
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import com.pnhung.icarchecking.view.viewmodel.CommonViewModel

class M007TrackingFrg : BaseFragment<FrgM007TrackingBinding, CommonViewModel>() {
    override fun initViews() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.frg_map) as SupportMapFragment
        mapFragment.getMapAsync {
            ProgressLoading.show(mContext, false)
            MapManager.getInstance().initMap(it)
            if (mData != null) {
                val carInfo = mData as CarInfoEntity
                binding?.includeActionBar?.tvCarNumber?.text = carInfo.carNumber
                binding?.includeActionBar?.ivBack?.setOnClickListener(this)
                MapManager.getInstance().showCarTracking(carInfo)
            }
        }

    }

    override fun initBinding(mRootView: View): FrgM007TrackingBinding? {
        return FrgM007TrackingBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m007_tracking
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                callBack?.showFrg(TAG, M003MenuMainFrg.TAG, false)
            }
        }
    }

    companion object {
        val TAG: String = M007TrackingFrg::class.java.name
    }

}
