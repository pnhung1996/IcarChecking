package com.pnhung.icarchecking.view.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.ViewM003UserInfoDialogBinding
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel

class UserInfoDialog(context: Context, callBack: OnActionCallBack) : BaseDialog<ViewM003UserInfoDialogBinding,BaseViewModel, Any?>(context, R.style.dialog_style_anim){
    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mCallBack = callBack
    }
    override fun initViews() {
        val userName = CommonUtils.getInstance().getPref(BaseViewModel.USER_NAME)
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)

        val tvUserName = findViewById<TextView>(R.id.tv_user_name)
        tvUserName.text = userName ?: "Chưa cập nhật tên"

        val tvPhone = findViewById<TextView>(R.id.tv_phone)
        tvPhone.text = phone

        findViewById<TextView>(R.id.tv_change_password, this)
        findViewById<TextView>(R.id.tv_update, this)
        findViewById<ImageView>(R.id.iv_back, this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.tv_change_password -> {
                goToChangePasswordDialog()
                dismiss()
            }

            R.id.tv_update -> {
                goToUpdateUserNameDialog()
                dismiss()
            }

            R.id.iv_back -> {
                dismiss()
            }
        }
    }

    private fun goToChangePasswordDialog() {
        val changePasswordDialog = ChangePasswordDialog(mContext!!)
        changePasswordDialog.show()
    }

    private fun goToUpdateUserNameDialog() {
        val updateUserNameDialog = UpdateUserNameDialog(mContext!!, mCallBack!!)
        updateUserNameDialog.show()
    }

    override fun getLayoutId(): Int {
        return R.layout.view_m003_user_info_dialog
    }

    override fun initViewBinding(view: View): ViewM003UserInfoDialogBinding {
        return ViewM003UserInfoDialogBinding.bind(view)
    }
}