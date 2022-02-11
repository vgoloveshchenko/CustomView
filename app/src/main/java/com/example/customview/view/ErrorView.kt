package com.example.customview.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.setPadding
import com.example.customview.R
import com.example.customview.databinding.LayoutErrorBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding = LayoutErrorBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setPadding(context.resources.getDimensionPixelSize(R.dimen.error_view_padding))
        setBackgroundResource(R.drawable.background_error_view)

        context.withStyledAttributes(attrs, R.styleable.ErrorView, defStyleAttr, 0) {
            binding.title.text = getString(R.styleable.ErrorView_title) ?: DEFAULT_TEXT
            binding.button.text = getString(R.styleable.ErrorView_buttonTitle) ?: DEFAULT_BUTTON_TEXT
        }
    }

    fun setOnButtonClickListener(action: () -> Unit) {
        binding.button.setOnClickListener {
            action()
        }
    }

    companion object {
        private const val DEFAULT_TEXT = "Something went wrong"
        private const val DEFAULT_BUTTON_TEXT = "ok"
    }
}