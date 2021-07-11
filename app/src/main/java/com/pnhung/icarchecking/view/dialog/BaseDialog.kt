package com.pnhung.icarchecking.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.pnhung.icarchecking.App
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.Storage
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel

abstract class BaseDialog<K : ViewBinding, T : BaseViewModel, M> : Dialog, View.OnClickListener, OnActionCallBack {
    protected var isAnimEnd = true
    protected var mView: View? = null
    protected var mAnim: Animation? = null
    protected var mModel: T? = null
    var mData: M? = null
    protected var mContext: Context? = null
    var mCallBack: OnActionCallBack? = null
    lateinit var mBinding: K

    constructor(
        context: Context,
        data: M?,
        owner: ViewModelStoreOwner? = null,
        clazz: Class<T>?
    ) : super(context) {
        mContext = context
        initCommon(data, owner, clazz)
    }

    constructor(
        context: Context,
        data: M?,
        theme: Int,
        owner: ViewModelStoreOwner? = null,
        clazz: Class<T>?
    ) : super(context, theme) {
        mContext = context
        initCommon(data, owner, clazz)
    }

    constructor(context: Context, data: M?, theme: Int) : this(context, data, theme, null, null)

    constructor(context: Context) : this(context, R.style.Theme_GPRSCarLocation) {}

    constructor(context: Context, data: M) : this(context, data, null, null)

    constructor(context: Context, theme: Int) : this(context, null, theme) {}

    fun <K : View?> findViewById(id: Int, event: View.OnClickListener?): K? {
        val v: K = findViewById(id)
        if (v != null && event != null) {
            v.setOnClickListener(event)
        }
        return v
    }

    protected val storage: Storage
        protected get() = App.getInstance().getStorage()

    private fun initCommon(data: M?, owner: ViewModelStoreOwner?, clazz: Class<T>?) {
        mData = data
        val view = LayoutInflater.from(mContext).inflate(getLayoutId(), null)
        mBinding = initViewBinding(view)
        setContentView(view)
        mAnim = AnimationUtils.loadAnimation(App.getInstance(), R.anim.anim_state)
        mAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                onClickView(mView)
                isAnimEnd = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        if (owner != null && clazz != null) {
            mModel = ViewModelProvider(owner).get(clazz)
        }
        setCanceledOnTouchOutside(false)
        initViews()
    }

    abstract fun initViewBinding(view: View): K

    protected open fun onClickView(mView: View?) {
        //do nothing
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

    protected abstract fun initViews()
    protected abstract fun getLayoutId(): Int
    override fun onClick(view: View) {
        if (!isAnimEnd) return
        isAnimEnd = false
        mView = view
        view.startAnimation(mAnim)
    }

    override fun showWarnNoInternet() {
        Toast.makeText(mContext, "Mạng không khả dụng, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT)
            .show()
    }

    companion object{
        const val SYSTEM_ERRORR = "Có lỗi xảy ra"
    }
}