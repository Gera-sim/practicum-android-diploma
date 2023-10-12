package ru.practicum.android.diploma.filters.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.filters.presentation.rv.IndustryAdapter
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel

class ChooseIndustry : Fragment() {
    private lateinit var binding: FragmentIndustryBinding
    private lateinit var industryAdapter: IndustryAdapter
    private var industryList = ArrayList<Industry>()
    private val viewModel by viewModel<FiltersViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIndustryAdapter()
        getIndustry()
        observeViewModel()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.chooseIndustryResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                ChooseIndustryResult.EmptyResult -> println("Pusto")
                is ChooseIndustryResult.Error -> println(state.exception)
                ChooseIndustryResult.NoInternet -> println("no internet")
                is ChooseIndustryResult.Success -> showIndustry(state.response)
            }
        }
    }

    private fun initIndustryAdapter(): IndustryAdapter {
        industryAdapter = IndustryAdapter {

        }
        industryAdapter.industryList = industryList
        binding.industryRecycler.adapter = industryAdapter
        return industryAdapter
    }

    private fun getIndustry() {
        viewModel.getIndustry()
    }

    private fun showIndustry(industry: List<Industry>) {
        industryList.clear()
        industryList.addAll(industry)
        industryAdapter.notifyDataSetChanged()
    }
}