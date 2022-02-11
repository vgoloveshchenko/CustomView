package com.example.customview.fragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.customview.databinding.FragmentAnimationBinding
import kotlin.random.Random

class AnimationFragment : Fragment() {

    private var _binding: FragmentAnimationBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAnimationBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonAlpha.setOnClickListener {
                TransitionManager.beginDelayedTransition(root)
                textAlpha.isVisible = !textAlpha.isVisible
            }

            buttonChanges.setOnClickListener {
                textChanges.setTextAnimation(Random.nextInt(0, 99999).toString())
            }

            buttonRotation.setOnClickListener {
                textRotation.animate()
                    .setDuration(500)
                    .rotationBy(90f)
                    .start()
            }

            ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, insets ->
                appBar.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                )
                insets
            }

            toolbar.setupWithNavController(findNavController())
        }
    }

    private fun TextView.setTextAnimation(text: String, duration: Long = 300) {
        fadeOutAnimation(duration) {
            this.text = text
            fadeInAnimation(duration)
        }
    }

    private fun View.fadeOutAnimation(
        duration: Long = 300,
        visibility: Int = View.INVISIBLE,
        completion: () -> Unit
    ) {
        animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                this.visibility = visibility
                completion()
            }
            .start()
    }

    private fun View.fadeInAnimation(duration: Long = 300) {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}