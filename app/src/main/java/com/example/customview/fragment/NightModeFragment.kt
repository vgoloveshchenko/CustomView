package com.example.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.customview.databinding.FragmentNightModeBinding
import com.example.customview.manager.SharedPrefsManager
import com.example.customview.model.NightMode
import org.koin.android.ext.android.inject

class NightModeFragment : Fragment() {

    private var _binding: FragmentNightModeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val prefsManager: SharedPrefsManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNightModeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            when (prefsManager.nightMode) {
                NightMode.DARK -> buttonDarkMode
                NightMode.LIGHT -> buttonLightMode
                NightMode.SYSTEM -> buttonSystemMode
            }.isChecked = true

            radioGroup.setOnCheckedChangeListener { _, buttonId ->
                val (prefsMode, systemMode) = when (buttonId) {
                    buttonDarkMode.id -> NightMode.DARK to AppCompatDelegate.MODE_NIGHT_YES
                    buttonLightMode.id -> NightMode.LIGHT to AppCompatDelegate.MODE_NIGHT_NO
                    buttonSystemMode.id -> NightMode.SYSTEM to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else -> error("incorrect buttonId $buttonId")
                }

                prefsManager.nightMode = prefsMode
                AppCompatDelegate.setDefaultNightMode(systemMode)
            }

            ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, insets ->
                appBar.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                )
                insets
            }

            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}