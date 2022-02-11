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
import androidx.navigation.ui.setupWithNavController
import com.example.customview.databinding.FragmentLanguageBinding
import com.example.customview.manager.SharedPrefsManager
import com.example.customview.model.Language
import org.koin.android.ext.android.inject

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val prefsManager: SharedPrefsManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLanguageBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            when (prefsManager.language) {
                Language.EN -> buttonEn
                Language.RU -> buttonRu
            }.isChecked = true

            radioGroup.setOnCheckedChangeListener { _, buttonId ->
                val language = when (buttonId) {
                    buttonEn.id -> Language.EN
                    buttonRu.id -> Language.RU
                    else -> error("incorrect buttonId $buttonId")
                }
                prefsManager.language = language

                activity?.recreate()
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