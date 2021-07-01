package com.pnhung.icarchecking.view.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.callback.OnAlertCallBack
import com.pnhung.icarchecking.view.callback.OnHomeCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel

abstract class BaseFragment<K : ViewBinding, V : BaseViewModel> : Fragment(), View.OnClickListener,
    OnActionCallBack {
    lateinit var mContext: Context
    lateinit var mRootView: View
    var callBack: OnHomeCallBack? = null
    var mData: Any? = null
    lateinit var mViewModel: V
    protected var binding: K? = null

    companion object {
        const val SYSTEM_ERRORR: String = "Có lỗi xảy ra"
    }

    final fun <T : View> findViewById(id: Int): T? {
        return findViewById(id, null)
    }

    final fun <T : View> findViewById(id: Int, event: View.OnClickListener?): T? {
        val v: T? = mRootView.findViewById(id)
        if (event != null) {
            v?.setOnClickListener(event)
        }
        return v
    }

    final override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(getLayoutId(), container, false)
        mViewModel = ViewModelProvider(this).get(getViewModelClass())
        mViewModel.setCallBack(this)
        binding = initBinding(mRootView)
        initViews()
        return mRootView
    }

    open fun doClickView(v: View?) {

    }

    override fun callBack(key: String, data: Any?) {
        if (key == BaseViewModel.KEY_NOTIFY) {
            showNotify((data ?: SYSTEM_ERRORR) as String)
        } else if (key == BaseViewModel.KEY_ERROR) {
            showNotify("SYSTEM ERROR : $data")
        }
    }

    private fun showNotify(text: String) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
    }

    final override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_state))
        doClickView(v)
    }

    abstract fun initViews()

    abstract fun initBinding(mRootView: View): K?

    abstract fun getViewModelClass(): Class<V>

    protected abstract fun getLayoutId(): Int

    protected fun showAlert(
        title: String?,
        msg: String?,
        textBt1: String?,
        textBt2: String?,
        callBack: OnAlertCallBack?
    ) {
        val alert: AlertDialog = AlertDialog.Builder(mContext).create()
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        if (textBt1 != null) {
            alert.setButton(
                AlertDialog.BUTTON_POSITIVE,
                textBt1
            ) { dialog, which ->
                callBack?.callBack(AlertDialog.BUTTON_POSITIVE, null)
            }
        }
        if (textBt2 != null) {
            alert.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                textBt2
            ) { dialog, which ->
                callBack?.callBack(AlertDialog.BUTTON_NEGATIVE, null)
            }
        }
        alert.show()
    }

}