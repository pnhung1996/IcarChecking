package com.pnhung.icarchecking.view.dialog

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import com.pnhung.icarchecking.view.viewmodel.UpdateUserNameViewModel

class UpdateUserNameDialog(context: Context, callBack: OnActionCallBack) :
    BaseDialog<UpdateUserNameViewModel, Any?>(
        context,
        null,
        R.style.dialog_style_anim,
        ViewModelStoreOwner { ViewModelStore() },
        UpdateUserNameViewModel::class.java
    ) {
    init {
        mModel?.setCallBack(callBack)
    }

    private var edtUserName: EditText? = null
    override fun initViews() {
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)

        val tvPhone = findViewById<TextView>(R.id.tv_phone)
        tvPhone.text = phone

        edtUserName = findViewById(R.id.edt_user_name)

        findViewById<TextView>(R.id.tv_update, this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_update -> {
                updateUserName()
                mCallBack?.callBack(
                    UpdateUserNameViewModel.API_KEY_UPDATE_USERNAME,
                    edtUserName?.text.toString()
                )
                dismiss()
            }
        }
    }

    private fun updateUserName() {
        val newUserName = edtUserName?.text.toString()
        if (newUserName.isEmpty()) {
            Toast.makeText(mContext, "Không để trống dữ liệu", Toast.LENGTH_SHORT).show()
            return
        }
        mModel?.updateUserName(newUserName)
    }

    override fun getLayoutId(): Int {
        return R.layout.view_m003_update_user_name_dialog
    }
}