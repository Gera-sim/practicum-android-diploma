package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO
import ru.practicum.android.diploma.filters.domain.models.ChooseResult
import ru.practicum.android.diploma.filters.presentation.rv.CountryAdapter
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class ChooseCountryFragment : Fragment() {
    companion object {
        const val KEY = "KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
        const val FILTER_COUNTRY = "FILTER_COUNTRY"
        const val FILTER_KEY = "FILTER_KEY"
    }

    private lateinit var binding: FragmentSelectCountryBinding
    private lateinit var countryAdapter: CountryAdapter
    private val viewModel by viewModel<FiltersViewModel>()
    private var countryList = ArrayList<AreasDTO>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSelectCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCountryAdapter()
        getCountry()
        observeViewModel()
        binding.btnBack.setOnClickListener {
            setResult(AreasDTO.emptyArea)
        }
    }

    private fun initCountryAdapter(): CountryAdapter {
        countryAdapter = CountryAdapter {
            setResult(it)
        }
        countryAdapter.countryList = countryList
        binding.regionRecycler.adapter = countryAdapter
        return countryAdapter
    }

    private fun setResult(country: AreasDTO) {
        setFragmentResult(
            KEY,
            bundleOf(BUNDLE_KEY to Json.encodeToString(country))
        )
        setFragmentResult(
            FILTER_KEY, bundleOf(FILTER_COUNTRY to Json.encodeToString(country))
        )
        findNavController().navigateUp()
    }

    private fun observeViewModel() {
        viewModel.chooseResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChooseResult.EmptyResult -> showError()
                is ChooseResult.Error -> showError()
                ChooseResult.NoInternet -> showError()
                is ChooseResult.Success -> showCountry(state.response)
            }
        }
    }

    private fun showCountry(countries: List<AreasDTO>) {
        hideError()
        countryList.clear()
        countryList.addAll(countries.slice(listOf(0, 1, 2, 3, 4, 5, 7, 8, 6)))
        countryAdapter.notifyDataSetChanged()
    }

    private fun showError() {
        binding.noInternetImage.visibility = View.VISIBLE
        binding.noInternetText.visibility = View.VISIBLE
        binding.regionRecycler.visibility = View.GONE
    }

    private fun hideError() {
        binding.noInternetImage.visibility = View.GONE
        binding.noInternetText.visibility = View.GONE
        binding.regionRecycler.visibility = View.VISIBLE
    }

    private fun getCountry() {
        viewModel.getCountry()
    }
}