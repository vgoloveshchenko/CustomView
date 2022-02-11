package com.example.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.customview.databinding.FragmentInsetsBinding
import com.example.customview.extensions.Offset
import com.example.customview.extensions.doOnApplyAnimatedWindowInsets
import com.example.customview.extensions.doOnApplyWindowInsets
import com.google.android.material.textfield.TextInputLayout


class InsetsFragment : Fragment() {

    private var _binding: FragmentInsetsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentInsetsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setupWithNavController(findNavController())

            ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, insets ->
                appBar.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                )
                insets
            }

            inputLayoutCommon.setInsetsListener()
            inputLayoutAnimated.setAnimatedInsetsListener()

            switchView.setOnCheckedChangeListener { _, isChecked ->
                inputLayoutCommon.isVisible = !isChecked
                inputLayoutAnimated.isVisible = isChecked
            }

            button.setOnClickListener {
                toggleKeyboard()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun TextInputLayout.setInsetsListener() {
        doOnApplyWindowInsets(
            initialOffset = Offset(bottom = marginBottom),
            action = ::applyImeAndNavigationBarInsets
        )
    }

    private fun TextInputLayout.setAnimatedInsetsListener() {
        doOnApplyAnimatedWindowInsets(
            initialOffset = Offset(bottom = marginBottom),
            action = ::applyImeAndNavigationBarInsets
        )
    }

    private fun applyImeAndNavigationBarInsets(
        view: View,
        insets: WindowInsetsCompat,
        offset: Offset
    ): WindowInsetsCompat {
        val navBarOrImeInsets = insets.getInsets(
            WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.navigationBars()
        )
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = navBarOrImeInsets.bottom + offset.bottom
        }
        return insets
    }

    private fun toggleKeyboard() {
        WindowCompat.getInsetsController(requireActivity().window, binding.root)
            ?.let {
                if (!isKeyboardVisible) {
                    it.show(WindowInsetsCompat.Type.ime())
                } else {
                    it.hide(WindowInsetsCompat.Type.ime())
                }
            }
    }

    private val isKeyboardVisible: Boolean
        get() = ViewCompat
            .getRootWindowInsets(binding.root)
            ?.isVisible(WindowInsetsCompat.Type.ime())
            ?: false
}