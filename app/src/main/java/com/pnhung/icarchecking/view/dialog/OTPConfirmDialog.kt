package com.pnhung.icarchecking.view.dialog

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.internal.LinkedTreeMap
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.databinding.ViewM001OtpDialogBinding
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.viewmodel.BaseViewModel
import org.json.JSONObject

class OTPConfirmDialog(context: Context, data: String?, callBack: OnActionCallBack) :
    BaseDialog<ViewM001OtpDialogBinding,BaseViewModel, String?>(context, data, R.style.dialog_style_anim) {
    private lateinit var edtOTP : EditText
    companion object {
        const val KEY_CONFIRM_OTP = "KEY_CONFIRM_OTP"
        private const val TITLE = "Nhập mã xác nhận được gửi đến\n\n%s"
    }

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mCallBack = callBack
    }

    override fun initViews() {
        val tvOTPSent : TextView = findViewById(R.id.tv_otp_sent)
        tvOTPSent.setText(String.format(TITLE, mData))

        val btnConfirm : TextView = findViewById(R.id.btn_confirm)
        btnConfirm.setOnClickListener(this)

        val btnBack : ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener(this)

        edtOTP = findViewById(R.id.edt_enter_otp)
    }

    override fun getLayoutId(): Int {
        return R.layout.view_m001_otp_dialog
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_confirm -> {
                Log.i("OTPConfirmDialog", "sending callBack")
                if(edtOTP.text.toString().isEmpty()){
                    Toast.makeText(mContext, "Không để trống ô nhập", Toast.LENGTH_SHORT).show()
                    return
                }
                mCallBack?.callBack(KEY_CONFIRM_OTP, edtOTP.text)
            }
            R.id.btn_back -> dismiss()
        }
    }

    override fun initViewBinding(view: View): ViewM001OtpDialogBinding {
        return ViewM001OtpDialogBinding.bind(view)
    }
}