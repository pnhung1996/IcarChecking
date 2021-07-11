package com.pnhung.icarchecking.view.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.ViewCarInfoBinding
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel

class CarInfoDialog(context: Context, carInfoEntity: CarInfoEntity, callBack: OnActionCallBack) :
    BaseDialog<ViewCarInfoBinding, BaseViewModel, CarInfoEntity>(
        context,
        carInfoEntity,
        R.style.dialog_style_anim
    ) {

    init {
        mCallBack = callBack
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun initViews() {
        mBinding.tvCarNumber.text = mData?.carNumber
        mBinding.tvCarBrand.text = mData?.carBrand

        val activeStatus =
            if (mData?.activeStatus == "offline") "Đang dừng đỗ" else mData?.activeStatus
        mBinding.tvCarStatus.text = activeStatus

        mBinding.tvCarSpeed.text = mData?.lastSpeed
        mBinding.tvCarAddress.text = mData?.lastAddress

        mBinding.ivBack.setOnClickListener(this)
        mBinding.tvObserve.setOnClickListener(this)
        mBinding.tvHistory.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                dismiss()
            }
            R.id.tv_observe -> {
                goToTrackingScreen(mData)
            }
            R.id.tv_history -> {
                goToTimeLineScreen(mData)
            }
        }
    }

    private fun goToTimeLineScreen(mData: CarInfoEntity?) {
        mCallBack?.callBack(KEY_SHOW_TIME_LINE, mData)
        dismiss()
    }

    private fun goToTrackingScreen(mData: CarInfoEntity?) {
        mCallBack?.callBack(KEY_SHOW_TRACKING, mData)
        dismiss()
    }

    override fun getLayoutId(): Int {
        return R.layout.view_car_info
    }

    override fun initViewBinding(view: View): ViewCarInfoBinding {
        return ViewCarInfoBinding.bind(view)
    }

    fun reload(carInfoEntity: CarInfoEntity) {
        mData = carInfoEntity
        initViews()
    }

    companion object {
        const val KEY_SHOW_TRACKING = "KEY_SHOW_TRACKING"
        const val KEY_SHOW_TIME_LINE = "KEY_SHOW_TIME_LINE"
    }
}