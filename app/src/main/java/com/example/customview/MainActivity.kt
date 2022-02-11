package com.example.customview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.example.customview.extensions.applySelectedAppLanguage
import com.example.customview.manager.SharedPrefsManager
import com.example.customview.model.NightMode
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val prefsManager: SharedPrefsManager by inject()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.applySelectedAppLanguage(prefsManager.language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        AppCompatDelegate.setDefaultNightMode(
            when (prefsManager.nightMode) {
                NightMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                NightMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                NightMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}