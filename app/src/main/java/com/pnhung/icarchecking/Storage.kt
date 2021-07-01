package com.pnhung.icarchecking

import android.annotation.SuppressLint
import android.location.Location
import com.pnhung.icarchecking.view.MapManager

class Storage {
    fun clearAll() {
        myPosition = null
    }

    var myPosition : Location? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: Storage? = null

        fun getInstance(): Storage {
            if (INSTANCE == null) {
                INSTANCE = Storage()
            }
            return INSTANCE!!
        }
    }
}