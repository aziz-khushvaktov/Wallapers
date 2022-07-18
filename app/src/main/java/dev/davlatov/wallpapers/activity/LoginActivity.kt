package dev.davlatov.wallpapers.activity

import android.os.Bundle
import dev.davlatov.wallpapers.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!myCheckInternetReceiver.hasInternet(this)) {
//            startInternetActivity()
//        }
        setContentView(R.layout.activity_login)
        installAppLang()
    }
}