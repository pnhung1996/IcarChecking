package com.pnhung.icarchecking.view.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.FrgM004TimeLineBinding
import com.pnhung.icarchecking.view.adapter.ListDayApdapter
import com.pnhung.icarchecking.view.api.model.ListDayModelRes
import com.pnhung.icarchecking.view.api.model.TimeLineDetailModelRes
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity
import com.pnhung.icarchecking.view.api.model.entities.LocationInfoEntity
import com.pnhung.icarchecking.view.viewmodel.M004TimeLineViewModel

class M004TimeLineFrg : BaseFragment<FrgM004TimeLineBinding, M004TimeLineViewModel>() {
    override fun initViews() {
        val carInfoEntity = mData as CarInfoEntity
        mViewModel.getListDayByCar(carInfoEntity.id!!)
        binding?.includeTimeLine?.ivBack?.setOnClickListener(this)
        binding?.includeTimeLine?.ivSearch?.setOnClickListener(this)
    }

    override fun initBinding(mRootView: View): FrgM004TimeLineBinding {
        return FrgM004TimeLineBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M004TimeLineViewModel> {
        return M004TimeLineViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m004_time_line
    }

    override fun callBack(key: String, data: Any?) {
        when (key) {
            M004TimeLineViewModel.API_KEY_GET_GROUP_DAY -> {
                val listDayByCar = (data as ListDayModelRes).data as ArrayList<String>
                val listDayAdapter = ListDayApdapter(mContext, listDayByCar, this)
                binding?.rvListDay?.adapter = listDayAdapter
                binding?.rvListDay?.layoutManager = GridLayoutManager(mContext, 1)
            }
            ListDayApdapter.KEY_SHOW_TIME_LINE_DETAIL -> {
                mViewModel.getTimeLineDetail(mData as CarInfoEntity, data as String)
            }
            M004TimeLineViewModel.API_KEY_GET_TIME_LINE_DETAIL -> {
                val listTimeLineDetail =
                    (data as TimeLineDetailModelRes).data as List<LocationInfoEntity>
                onShowTimeLineDetailCallBack?.showTimeLineDetail(listTimeLineDetail)
            }
        }
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                callBack?.showFrg(TAG, M003MenuMainFrg.TAG, false)
            }
            R.id.iv_search -> {
            }
        }
    }

    companion object {
        var onShowTimeLineDetailCallBack : OnShowTimeLineDetailCallBack? = null
        val TAG: String = M004TimeLineFrg::class.java.name
    }

    interface OnShowTimeLineDetailCallBack {
        fun showTimeLineDetail(listTimeLineDetail: List<LocationInfoEntity>)
    }
}