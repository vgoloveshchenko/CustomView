package com.example.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.customview.R
import com.example.customview.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonNightMode.setOnClickListener {
                findNavController().navigate(R.id.to_night_mode)
            }
            buttonLanguage.setOnClickListener {
                findNavController().navigate(R.id.to_language)
            }
            buttonAnimation.setOnClickListener {
                findNavController().navigate(R.id.to_animation)
            }
            buttonCustomView.setOnClickListener {
                findNavController().navigate(R.id.to_custom_view)
            }
            buttonInsets.setOnClickListener {
                findNavController().navigate(R.id.to_insets)
            }

            ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, insets ->
                appBar.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                )
                insets
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}