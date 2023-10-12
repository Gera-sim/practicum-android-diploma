package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.filters.presentation.view_model.FiltersViewModel
import ru.practicum.android.diploma.search.presentation.view_model.SearchViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        FiltersViewModel(
            filterInteractor = get(),
            chooseCountryInteractor = get(),
            chooseRegionInteractor = get(),
            chooseIndustryInteractor = get(),
        )
    }
}
