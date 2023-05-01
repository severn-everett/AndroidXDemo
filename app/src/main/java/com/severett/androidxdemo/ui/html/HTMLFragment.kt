package com.severett.androidxdemo.ui.html

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.severett.androidxdemo.R
import com.severett.androidxdemo.databinding.FragmentHtmlBinding

class HTMLFragment : Fragment() {
    private var _binding: FragmentHtmlBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val htmlViewModel = ViewModelProvider(
            this
        )[HTMLViewModel::class.java]
        _binding = FragmentHtmlBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.checkBoxBold.setOnCheckedChangeListener { _, isChecked ->
            htmlViewModel.isBold.value = isChecked
        }
        binding.checkboxStrikethrough.setOnCheckedChangeListener { _, isChecked ->
            htmlViewModel.isStrikethrough.value = isChecked
        }
        binding.checkboxStrikethrough.paintFlags =
            binding.checkboxStrikethrough.paintFlags or STRIKE_THRU_TEXT_FLAG
        binding.checkboxUnderlined.setOnCheckedChangeListener { _, isChecked ->
            htmlViewModel.isUnderlined.value = isChecked
        }

        binding.generateHTMLButton.setOnClickListener {
            val helloContent = resources.getString(R.string.content_html_hello)
            val linkContent = resources.getString(R.string.content_html_link)
            htmlViewModel.name.value = binding.editTextName.text.toString()
            binding.generatedHTMLDisplay.text = htmlViewModel.generateHTML(
                helloContent,
                linkContent
            )
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}