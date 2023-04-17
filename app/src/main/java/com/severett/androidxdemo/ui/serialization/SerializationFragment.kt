package com.severett.androidxdemo.ui.serialization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.severett.androidxdemo.databinding.FragmentSerializationBinding

class SerializationFragment : Fragment() {

    private var _binding: FragmentSerializationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val serializationViewModel =
            ViewModelProvider(this).get(SerializationViewModel::class.java)

        _binding = FragmentSerializationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSerialization
        serializationViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}