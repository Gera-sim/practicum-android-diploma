package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.ChangeTextFieldUtil
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.BUNDLE_KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseCountryFragment.Companion.KEY
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.KEY_R
import ru.practicum.android.diploma.filters.presentation.ui.ChooseRegionFragment.Companion.REGION_KEY

class ChoosePlaceWorkFragment : Fragment() {
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var country: String
    private lateinit var region: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setFilters()
        showChooseBtn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCountry.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseCountryFragment)
        }
        binding.countryClearBtn.setOnClickListener {
            binding.countryEditText.text?.clear()
            changeCountryField()
            showChooseBtn()
        }
        binding.btnRegion.setOnClickListener {
            findNavController().navigate(R.id.action_choosePlaceWorkFragment_to_chooseRegionFragment)
        }
        binding.regionClearBtn.setOnClickListener {
            binding.regionEditText.text?.clear()
            changeRegionField()
            showChooseBtn()
        }
        binding.btnChoose.setOnClickListener {
            chooseFilters()
        }
    }

    private fun setFilters() {
        setFragmentResultListener(KEY)
        { _, bundle ->
            country = bundle.getString(BUNDLE_KEY).orEmpty()
            if (country.isNotEmpty()) {
                binding.countryEditText.setText(country)
            }
        }
        setFragmentResultListener(KEY_R)
        { _, bundle ->
            region = bundle.getString(REGION_KEY).orEmpty()
            if (region.isNotEmpty()) {
                binding.regionEditText.setText(region)
            }
        }
        changeCountryField()
        changeRegionField()
    }


    private fun changeCountryField() {
        ChangeTextFieldUtil.changeTextField(
            binding.countryEditText,
            binding.countryTextField,
            binding.countryClearBtn,
            requireContext(),
        )
    }

    private fun changeRegionField() {
        ChangeTextFieldUtil.changeTextField(
            binding.regionEditText,
            binding.regionTextField,
            binding.regionClearBtn,
            requireContext(),
        )
    }

    private fun chooseFilters() {
        /*  if (country.isNullOrEmpty()) {
              binding.btnChoose.visibility = View.GONE
          } else {
              binding.btnChoose.visibility = View.VISIBLE*/
        if (region.isNullOrEmpty()) {
            setFragmentResult(
                KEY_CHOOSE,
                bundleOf(PLACE_WORK to country)
            )
        } else {
            val result = "$country,$region"
            setFragmentResult(
                KEY_CHOOSE,
                bundleOf(PLACE_WORK to result)
            )
        }
        findNavController().navigateUp()
    }

    private fun showChooseBtn() {
        region = binding.regionEditText.text.toString()
        binding.btnChoose.isVisible = !binding.countryEditText.text.isNullOrEmpty()
    }

    companion object {
        const val KEY_CHOOSE = "key_choose"
        const val PLACE_WORK = "place_work"
    }
}