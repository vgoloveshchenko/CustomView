package com.example.customview.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach

fun View.doOnApplyWindowInsets(
    initialOffset: Offset = Offset(),
    action: (View, WindowInsetsCompat, Offset) -> WindowInsetsCompat
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        action(v, insets, initialOffset)
    }

    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        doOnAttach {
            it.requestApplyInsets()
        }
    }
}

fun View.doOnApplyAnimatedWindowInsets(
    initialOffset: Offset = Offset(),
    action: (View, WindowInsetsCompat, Offset) -> WindowInsetsCompat
) {
    fun setInitialInsets() {
        val initialInsets = checkNotNull(ViewCompat.getRootWindowInsets(this))
        action(this, initialInsets, initialOffset)
    }

    if (isAttachedToWindow) {
        setInitialInsets()
    } else {
        doOnAttach {
            setInitialInsets()
        }
    }

    ViewCompat.setWindowInsetsAnimationCallback(
        this,
        object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

            override fun onProgress(
                insets: WindowInsetsCompat,
                runningAnimations: MutableList<WindowInsetsAnimationCompat>
            ): WindowInsetsCompat {
                return action(this@doOnApplyAnimatedWindowInsets, insets, initialOffset)
            }
        }
    )
}

data class Offset(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
)