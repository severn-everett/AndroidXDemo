package com.severett.androidxdemo.ui.serialization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.severett.androidxdemo.R
import com.severett.androidxdemo.databinding.FragmentSerializationBinding
import com.severett.androidxdemo.model.Foo
import com.severett.androidxdemo.model.ThirdPartyFoo
import com.severett.androidxdemo.serde.ThirdPartyFooSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
        val serializationViewModel = ViewModelProvider(
            this
        )[SerializationViewModel::class.java]

        _binding = FragmentSerializationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.enterFizzLabel.text = resources.getString(R.string.input_serialization_fizz)
        binding.enterBazzLabel.text = resources.getString(R.string.input_serialization_bazz)
        binding.enterCountLabel.text = resources.getString(R.string.input_serialization_count)
        val serializedFooTextView = binding.serializedFooDisplay
        serializationViewModel.serializedFooText.observe(
            viewLifecycleOwner,
            serializedFooTextView::setText
        )
        val deserializedFooTextView = binding.deserializedFooDisplay
        serializationViewModel.deserializedFooText.observe(
            viewLifecycleOwner,
            deserializedFooTextView::setText
        )
        binding.radioTypeNormal.isChecked = true
        binding.radioTypeThirdParty.isChecked = false
        binding.runSerdeButton.setOnClickListener {
            val fizz = binding.editTextFizz.text.toString()
            val bazz = binding.editTextBazz.text.toString().split(",")
            val count = binding.editTextCount.text.toString().toUInt()
            val checkedType = binding.serializationTypeRadioGroup.checkedRadioButtonId
            val (encodedStr, decodedStr) = if (checkedType == binding.radioTypeNormal.id) {
                serdeFoo(fizz, bazz, count)
            } else {
                serdeThirdPartyFoo(fizz, bazz, count)
            }
            binding.serializedFooLabel.text = resources.getString(
                R.string.label_serialization_serialized
            )
            binding.serializedFooDisplay.text = encodedStr
            binding.deserializedFooLabel.text = resources.getString(
                R.string.label_serialization_deserialized
            )
            binding.deserializedFooDisplay.text = decodedStr
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun serdeFoo(fizz: String, bazz: List<String>, count: UInt): Pair<String, String> {
        val foo = Foo(fizz, bazz, count)
        val encodedFoo = Json.encodeToString(foo)
        val decodedFoo: Foo = Json.decodeFromString(encodedFoo)
        return encodedFoo to decodedFoo.toString()
    }

    private fun serdeThirdPartyFoo(
        fizz: String,
        bazz: List<String>,
        count: UInt
    ): Pair<String, String> {
        val foo = ThirdPartyFoo(fizz, bazz, count)
        val encodedFoo = Json.encodeToString(ThirdPartyFooSerializer, foo)
        val decodedFoo = Json.decodeFromString(ThirdPartyFooSerializer, encodedFoo)
        return encodedFoo to decodedFoo.toString()
    }
}