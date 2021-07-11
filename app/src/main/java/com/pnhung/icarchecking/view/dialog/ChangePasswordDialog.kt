package com.pnhung.icarchecking.view.dialog

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.ViewM003ChangePasswordDialogBinding
import com.pnhung.icarchecking.view.ProgressLoading
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import com.pnhung.icarchecking.view.viewmodel.ChangePasswordViewModel
import com.pnhung.icarchecking.view.viewmodel.UpdateUserNameViewModel

class ChangePasswordDialog(context: Context) :
    BaseDialog<ViewM003ChangePasswordDialogBinding,ChangePasswordViewModel, Any?>(
        context,
        null,
        R.style.dialog_style_anim,
        ViewModelStoreOwner { ViewModelStore() },
        ChangePasswordViewModel::class.java
    ) {
    private var edtOldPassword: EditText? = null
    private var edtNewPassword: EditText? = null
    private var edtConfirmNewPassword: EditText? = null
    private var oldPassword: String = ""
    private var newPassword: String = ""
    private var confirmNewPassword: String = ""

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mModel?.setCallBack(this)
    }

    override fun initViews() {
        edtOldPassword = findViewById(R.id.edt_old_password)
        edtNewPassword = findViewById(R.id.edt_new_password)
        edtConfirmNewPassword = findViewById(R.id.edt_confirm_new_password)

        findViewById<TextView>(R.id.tv_update, this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_update -> {
                changePassword()
            }
            R.id.iv_back -> {
                dismiss()
            }
        }
    }

    private fun changePassword() {
        oldPassword = edtOldPassword?.text.toString()
        newPassword = edtNewPassword?.text.toString()
        confirmNewPassword = edtConfirmNewPassword?.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(mContext, "Không để trống dữ liệu", Toast.LENGTH_SHORT).show()
            return
        }

        if (oldPassword.length < 6 || newPassword.length < 6) {
            Toast.makeText(mContext, "Mật khẩu cần có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }

        if(newPassword != confirmNewPassword){
            Toast.makeText(mContext, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show()
            return
        }
        ProgressLoading.show(mContext!!, false)
        mModel?.changePassword(oldPassword, newPassword)
    }

    override fun callBack(key: String, data: Any?) {
        when(key){
            ChangePasswordViewModel.API_KEY_CHANGE_PASSWORD -> {
                Toast.makeText(mContext, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            BaseViewModel.KEY_NOTIFY -> {
                Toast.makeText(mContext, "Sai mật khẩu cũ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.view_m003_change_password_dialog
    }

    override fun initViewBinding(view: View): ViewM003ChangePasswordDialogBinding {
        return ViewM003ChangePasswordDialogBinding.bind(view)
    }
}