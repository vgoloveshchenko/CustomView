package com.example.customview.koin

import com.example.customview.manager.SharedPrefsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { SharedPrefsManager(androidContext()) }
}