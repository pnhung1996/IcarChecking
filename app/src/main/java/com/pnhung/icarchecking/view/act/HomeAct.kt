package com.pnhung.icarchecking.view.act

import android.view.View
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.WebSocketUtil
import com.pnhung.icarchecking.databinding.ActHomeBinding
import com.pnhung.icarchecking.view.MapManager
import com.pnhung.icarchecking.view.fragment.M000SplashFrg

class HomeAct : BaseActivity<ActHomeBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.act_home
    }

    override fun initBinding(rootView: View): ActHomeBinding {
        return ActHomeBinding.bind(rootView)
    }

    override fun initViews() {
        MapManager.getInstance().mContext = this
        showFrg(TAG, M000SplashFrg.TAG, false)
    }

    override fun onDestroy() {
        WebSocketUtil.getInstance()?.disconnect()
        super.onDestroy()
    }

    companion object {
        private val TAG = HomeAct::class.java.name
    }
}