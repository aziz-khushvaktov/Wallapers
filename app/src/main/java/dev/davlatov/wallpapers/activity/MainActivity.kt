package dev.davlatov.wallpapers.activity

import android.os.Bundle
import dev.davlatov.wallpapers.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        installAppLang()
    }
}