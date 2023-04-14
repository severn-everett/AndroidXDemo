package com.severett.androidxdemo.ui.atomicfu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.severett.androidxdemo.databinding.FragmentAtomicfuBinding
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AtomicFUFragment : Fragment() {
    private val safeCounter = atomic(0)
    private var unsafeCounter = 0

    private var _binding: FragmentAtomicfuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val atomicFUViewModel = ViewModelProvider(this)[AtomicFUViewModel::class.java]

        _binding = FragmentAtomicfuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        atomicFUViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        runBlocking {
            (0 until 1000).map {
                CoroutineScope(Dispatchers.Default).launch {
                    safeCounter += 1
                    unsafeCounter += 1
                }
            }.forEach { it.join() }
        }
        atomicFUViewModel.text.value = "Safe | Unsafe: ${safeCounter.value} | $unsafeCounter"
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}