package com.severett.androidxdemo.ui.atomicfu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.severett.androidxdemo.R
import com.severett.androidxdemo.databinding.FragmentAtomicfuBinding
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val LIMIT = 10_000

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
        _binding = FragmentAtomicfuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.runRaceButton.setOnClickListener {
            val (safeValue, unsafeValue) = runRace()
            binding.safeValueLabel.text = resources.getString(
                R.string.content_atomicfu_safe_value,
                safeValue.toString()
            )
            binding.unsafeValueLabel.text = resources.getString(
                R.string.content_atomicfu_unsafe_value,
                unsafeValue.toString().padStart(5, ' ')
            )
        }

        binding.lockDemoButton.setOnClickListener {
            val lockedValue = lockDemo()
            binding.lockedValueLabel.text = resources.getString(
                R.string.content_atomicfu_locked_value,
                lockedValue.toString()
            )
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
            (0 until LIMIT).map {
                CoroutineScope(Dispatchers.Default).launch {
                    safeCounter += 1
                    unsafeCounter += 1
                }
            }.forEach { it.join() }
        }
        return safeCounter.value to unsafeCounter
    }

    private fun lockDemo(): Int {
        var unsafeValue = 0
        val lock = reentrantLock()
        runBlocking {
            (0 until LIMIT).map {
                CoroutineScope(Dispatchers.Default).launch {
                    lock.withLock { unsafeValue += 1 }
                }
            }.forEach { it.join() }
        }
        return unsafeValue
    }
}