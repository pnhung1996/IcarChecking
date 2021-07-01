package com.pnhung.icarchecking.view.act

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.view.callback.OnHomeCallBack
import com.pnhung.icarchecking.view.fragment.BaseFragment
import java.lang.reflect.Constructor

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), OnHomeCallBack {
    protected var binding: V? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView: View = LayoutInflater.from(this).inflate(getLayoutId(), null)
        setContentView(rootView)
        binding = initBinding(rootView)
        initViews()
    }

    abstract fun initViews()

    abstract fun initBinding(rootView: View): V?

    abstract fun getLayoutId(): Int

    protected final fun showNotify(sms: String) {
        Toast.makeText(this, sms, Toast.LENGTH_SHORT).show()
    }

    override fun showFrg(backTag: String, data: Any?, tag: String, isBacked: Boolean) {
        try {
            val clazz = Class.forName(tag)
            val constructor = clazz.getConstructor()
            val frg = constructor.newInstance() as BaseFragment<*, *>

            frg.callBack = this
            frg.mData = data

            val trans: FragmentTransaction = supportFragmentManager.beginTransaction()
            if (isBacked) {
                trans.addToBackStack(backTag)
            }
            trans.replace(R.id.ln_main, frg). commit()
        } catch (e: Exception) {
            showNotify("Err: " + e.message)
            e.printStackTrace()
        }
    }

    override fun showFrg(backTag: String, tag: String, isBacked: Boolean) {
        showFrg(backTag, null, tag, isBacked)
    }

    override fun closeApp() {
        finish()
    }

}