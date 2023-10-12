package ru.practicum.android.diploma.search.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.Constants.CLICK_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.Constants.SEARCH_DEBOUNCE_DELAY_MILLIS
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.core.root.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItem
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.search.presentation.models.SearchUIState
import ru.practicum.android.diploma.search.presentation.rv.VacancyAdapter
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyFragment

class SearchFragment : Fragment() {
    private val viewModel by viewModel<SearchViewModel>()
    private val vacancyList = ArrayList<VacancyItem>()
    private val handler = Handler(Looper.getMainLooper())
    private val vacancySearchRunnable = Runnable { viewModel.searchVacancies(binding.searchEditText.text.toString()) }
    private lateinit var onVacancyClickDebounce: (VacancyItem) -> Unit
    private val vacancyAdapter = VacancyAdapter(vacancyList)
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        onVacancyClickDebounce = debounce<VacancyItem>(CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false) { item ->
            navigateToVacancyDetail(item)
        }
    }

    override fun onResume() {
        super.onResume()
        setupDefaultUI()
        binding.searchEditText.text.clear()
    }

    private fun setupViews() {
        binding.iconFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filtersFragment)
        }
        vacancyAdapter.itemClickListener = { position, vacancy ->
            (activity as? RootActivity)?.animateBottomNavigationView()
            onVacancyClickDebounce(vacancy)
        }

        setupDefaultUI()
        setupTextWatcher()
        setupRecyclerView()
    }

    private fun setupDefaultUI() {
        with(binding) {
            iconSearch.setImageResource(R.drawable.ic_search)
            searchPlaceholder.setImageResource(R.drawable.placeholder_search)
            searchPlaceholder.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
        vacancyList.clear()
        vacancyAdapter.notifyDataSetChanged()
        handler.removeCallbacks(vacancySearchRunnable)
    }

    private fun observeViewModel() {
        viewModel.searchVacancyResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchVacancyResult.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                SearchVacancyResult.EmptyResult -> updateUI(SearchUIState.EMPTY_SEARCH)
                SearchVacancyResult.NoInternet -> updateUI(SearchUIState.NO_INTERNET)
                is SearchVacancyResult.Success -> showVacancy(state.response.items)
            }
        }
    }
    private fun updateUI(searchUIState: SearchUIState) {
        with(binding) {
            recyclerView.visibility = View.GONE

            when (searchUIState) {
                SearchUIState.CONNECTION_ERROR -> {
                    searchPlaceholder.setImageResource(0)
                    searchPlaceholder.setImageResource(R.drawable.placeholder_sad)
                    vacancyList.clear()
                }
                SearchUIState.EMPTY_SEARCH -> {
                    searchPlaceholder.setImageResource(0)
                    searchPlaceholder.setImageResource(R.drawable.placeholder_cat)
                    vacancyList.clear()
                }
                SearchUIState.NO_INTERNET -> {
                    searchPlaceholder.setImageResource(0)
                    searchPlaceholder.setImageResource(R.drawable.placeholder_skull)
                    vacancyList.clear()

                }
                SearchUIState.LOADING -> TODO()
            }
            searchPlaceholder.visibility = View.VISIBLE

            iconSearch.setImageResource(R.drawable.ic_search)
            vacancyList.clear()

            vacancyAdapter.notifyDataSetChanged()
            handler.removeCallbacks(vacancySearchRunnable)
        }
    }
    private fun showVacancy(items: List<VacancyItem>) {
        with(binding) {
            recyclerView.visibility = View.VISIBLE
            searchPlaceholder.visibility = View.GONE
        }
        vacancyList.clear()
        vacancyList.addAll(items)
        vacancyAdapter.notifyDataSetChanged()
    }
    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = vacancyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun setupTextWatcher() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    with(binding) {
                        recyclerView.visibility = View.GONE
                        searchPlaceholder.visibility = View.VISIBLE
                        iconSearch.setImageResource(R.drawable.ic_search)
                    }
                    vacancyList.clear()
                    vacancyAdapter.notifyDataSetChanged()
                    handler.removeCallbacks(vacancySearchRunnable)
                } else {
                    binding.iconSearch.setImageResource(R.drawable.ic_clear)
                    handler.removeCallbacks(vacancySearchRunnable)
                    handler.postDelayed(vacancySearchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun navigateToVacancyDetail(item: VacancyItem) {
        findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(item))
    }
}