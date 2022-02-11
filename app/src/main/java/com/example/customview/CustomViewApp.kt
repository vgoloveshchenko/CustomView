package com.example.customview

import android.app.Application
import android.content.Context
import com.example.customview.koin.appModule
import com.example.customview.language.LanguageAwareAppContext
import com.example.customview.manager.SharedPrefsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomViewApp : Application() {

    private val prefsManager: SharedPrefsManager by inject()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageAwareAppContext(base, application = this))
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CustomViewApp)
            modules(
                appModule
            )
        }

        prefsManager
            .languageFlow
            .onEach {
                (baseContext as LanguageAwareAppContext).appLanguage = it
            }
            .launchIn(CoroutineScope(Dispatchers.Main))
    }
}