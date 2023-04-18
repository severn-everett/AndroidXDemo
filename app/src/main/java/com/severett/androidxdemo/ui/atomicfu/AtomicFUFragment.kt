package com.severett.androidxdemo.ui.atomicfu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.severett.androidxdemo.R
import com.severett.androidxdemo.databinding.FragmentAtomicfuBinding
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AtomicFUFragment : Fragment() {
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
        atomicFUViewModel.safeResultVal.value = ""
        atomicFUViewModel.unsafeResultVal.value = ""

        _binding = FragmentAtomicfuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val safeValueView = binding.safeValueResult
        atomicFUViewModel.safeResultVal.observe(viewLifecycleOwner) {
            safeValueView.text = it
        }
        val unsafeValueView = binding.unsafeValueResult
        atomicFUViewModel.unsafeResultVal.observe(viewLifecycleOwner) {
            unsafeValueView.text = it
        }
        binding.runRaceButton.setOnClickListener {
            val (safeValue, unsafeValue) = runRace()
            binding.safeValueLabel.text = resources.getString(
                R.string.label_atomicfu_safe_value
            )
            binding.unsafeValueLabel.text = resources.getString(
                R.string.label_atomicfu_unsafe_value
            )
            atomicFUViewModel.safeResultVal.value = safeValue.toString()
            atomicFUViewModel.unsafeResultVal.value = unsafeValue.toString()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun runRace(): Pair<Int, Int> {
        val safeCounter = atomic(0)
        var unsafeCounter = 0
        runBlocking {
            (0 until 10_000).map {
                CoroutineScope(Dispatchers.Default).launch {
                    safeCounter += 1
                    unsafeCounter += 1
                }
            }.forEach { it.join() }
        }
        return safeCounter.value to unsafeCounter
    }
}