package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputFilter
import android.text.InputType
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.withStyledAttributes
import com.example.customview.R

class PasscodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var charCount: Int = DEFAULT_CHAR_COUNT
    private var charSpaceSize: Float = DEFAULT_CHAR_DIMENSION

    private val circlePaint = TextPaint().apply {
        style = Paint.Style.FILL
        textSize = this@PasscodeEditText.textSize
    }

    private val circleEmptyPaint = TextPaint().apply {
        style = Paint.Style.STROKE
        textSize = this@PasscodeEditText.textSize
        strokeWidth = 4f
    }

    private val textLength: Int
        get() = text?.length ?: 0

    private fun Context.themeColor(@AttrRes attrRes: Int) =
        TypedValue()
            .apply { theme.resolveAttribute(attrRes, this, true) }
            .data

    init {
        setBackgroundColor(Color.TRANSPARENT)

        context.withStyledAttributes(attrs, R.styleable.PasscodeEditText, defStyleAttr, 0) {
            charCount = getInt(
                R.styleable.PasscodeEditText_charCount,
                DEFAULT_CHAR_COUNT
            )
            charSpaceSize = getDimension(
                R.styleable.PasscodeEditText_charSpaceSize,
                DEFAULT_CHAR_DIMENSION
            )
            circlePaint.color = getColor(
                R.styleable.PasscodeEditText_charFilledColor,
                context.themeColor(R.attr.colorPrimary)
            )
            circleEmptyPaint.color = getColor(
                R.styleable.PasscodeEditText_charEmptyBorderColor,
                context.themeColor(R.attr.colorSecondary)
            )
        }

        filters = arrayOf(InputFilter.LengthFilter(charCount))
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }

    private val explicitViewWidth =
        (charSpaceSize * (charCount - 1) + textSize * charCount + circleEmptyPaint.strokeWidth * 2).toInt()


    /*
        MeasureSpec.EXACTLY означает, что пользователь жёстко задал значения размера,
         независимо от размера вашего View-компонента, вы должны установить определённую ширину и высоту;
        MeasureSpec.AT_MOST используется для создания вашего View-компонента в соответствии
        с размером родителя, поэтому он может быть настолько большим, насколько это возможно;
        MeasureSpec.UNSPECIFIED - на самом деле размер обёртки View-компонента.
        Таким образом, с этим параметром вы можете использовать желаемый размер, который вы расчитали выше.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = explicitViewWidth + paddingStart + paddingEnd
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val currentWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredWidth, widthSize)
            else -> desiredWidth
        }

        setMeasuredDimension(currentWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingStart - paddingEnd
        var startX = (availableWidth - explicitViewWidth) / 2

        repeat(charCount) { i ->
            val middle = startX + textSize / 2
            if (textLength > i) {
                canvas.drawCircle(middle, height.toFloat() / 2, textSize / 2, circlePaint)
            } else {
                canvas.drawCircle(middle, height.toFloat() / 2, textSize / 2, circleEmptyPaint)
            }
            startX += (textSize + charSpaceSize).toInt()
        }
    }

    companion object {
        private const val DEFAULT_CHAR_COUNT = 4
        private const val DEFAULT_CHAR_DIMENSION = 50f
    }
}