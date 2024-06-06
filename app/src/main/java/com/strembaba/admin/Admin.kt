package com.strembaba.admin

import android.app.Application

class Admin : Application() {

    override fun onCreate() {
        super.onCreate()
        admin = this
    }
    companion object{
        var admin: Admin? = null
            private set
    }
}