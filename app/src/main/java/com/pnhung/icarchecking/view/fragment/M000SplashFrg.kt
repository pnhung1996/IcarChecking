package com.pnhung.icarchecking.view.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.FrgM000SplashBinding
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import com.pnhung.icarchecking.view.viewmodel.M000SplashViewModel

class M000SplashFrg : BaseFragment<FrgM000SplashBinding, M000SplashViewModel>() {
    companion object {
        val TAG = M000SplashFrg::class.java.name
    }

    override fun initViews() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                val token = CommonUtils.getInstance().getPref(BaseViewModel.TOKEN)
                val phone = CommonUtils.getInstance().getPref(M002LoginFrg.PHONE)
                if (token == null || token.isEmpty() || phone == null) {
                    callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
                } else {
                    callBack?.showFrg(TAG, M003MenuMainFrg.TAG, false)
                    //CommonUtils.getInstance().clearPref(M002LoginFrg.PHONE)
                }
            }, 2000)
    }

    override fun initBinding(mRootView: View): FrgM000SplashBinding? {
        return null
    }

    override fun getViewModelClass(): Class<M000SplashViewModel> {
        return M000SplashViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m000_splash
    }
}
