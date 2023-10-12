package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentRegionsBinding
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.filters.presentation.rv.RegionAdapter
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel
import ru.practicum.android.diploma.search.data.dto.response_models.Area


class ChooseRegionFragment : Fragment() {
    private lateinit var binding: FragmentRegionsBinding
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var textWatcher: TextWatcher
    private val viewModel by viewModel<FiltersViewModel>()
    private var regionList = ArrayList<Area>()
    private var regionListSaved = ArrayList<Area>()
    private var regionListFilter = ArrayList<Area>()
    private lateinit var selected: Area
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCountryAdapter()
        getRegions()
        observeViewModel()
        initListeners()
    }

    private fun observeViewModel() {
        viewModel.chooseRegionResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseRegionsResult.EmptyResult -> {
                    showErrorList()
                }

                is ChooseRegionsResult.Error -> {
                    showErrorList()
                    println(state.exception)
                }

                ChooseRegionsResult.NoInternet -> {
                    showErrorList()
                }

                is ChooseRegionsResult.Success -> {
                    showRegions(state.response)
                }
            }
        }
    }

    private fun showRegions(regions: List<Area>) {
        regionList.clear()
        regionList.addAll(regions)
        regionAdapter.notifyDataSetChanged()
        binding.regionRecycler.visibility = View.VISIBLE
        regionListSaved.addAll(regions)
        binding.noInternetImage.visibility = View.GONE
        binding.noInternetText.visibility = View.GONE
    }

    private fun getRegions() {
        viewModel.getRegions()
    }

    private fun showErrorList() {
        binding.noInternetImage.visibility = View.VISIBLE
        binding.noInternetText.visibility = View.VISIBLE
        binding.regionRecycler.visibility = View.GONE
    }

    private fun initCountryAdapter(): RegionAdapter {
        regionAdapter = RegionAdapter {
            setResult(it)
            binding.selectBtn.visibility = View.VISIBLE
        }
        regionAdapter.regionList = regionList
        binding.regionRecycler.adapter = regionAdapter
        return regionAdapter
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                debounceSearch(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        binding.regionEditText.addTextChangedListener(textWatcher)
        binding.selectBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showErrorNothing() {
        binding.nothingSearchImage.visibility = View.VISIBLE
        binding.nothingSearchText.visibility = View.VISIBLE
    }

    private fun hideErrorNothing() {
        binding.nothingSearchImage.visibility = View.GONE
        binding.nothingSearchText.visibility = View.GONE
    }

    private fun debounceSearch(p0: String) {
        val onSearchDebounce = debounce<String>(
            SEARCH_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            true,
            action = { text ->
                if (text.isNullOrEmpty()) {
                    hideErrorNothing()
                    regionList.clear()
                    regionListFilter.clear()
                    regionList.addAll(regionListSaved)
                    regionAdapter.notifyDataSetChanged()
                } else {
                    regionList.clear()
                    regionListSaved.forEach {
                        if (it.name!!.lowercase() == text.lowercase()) {
                            regionListFilter.add(it)
                        }
                    }
                    if (regionListFilter.isEmpty()) {
                        showErrorNothing()
                    } else {
                        hideErrorNothing()
                    }
                    regionList.addAll(regionListFilter)
                    regionAdapter.notifyDataSetChanged()
                }
            })
        onSearchDebounce(p0)
    }

    private fun setResult(region: Area) {
        setFragmentResult(
            KEY_R,
            bundleOf(REGION_KEY to Json.encodeToString(region))
        )
    }

    companion object {
        const val KEY_R = "KEY_R"
        const val REGION_KEY = "REGION_KEY"
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }
}