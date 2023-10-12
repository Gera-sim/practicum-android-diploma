package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.impl.ChooseCountryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseIndustryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseRegionsInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor

val domainModule = module {
    single {
        SearchInteractor(
            searchRepository = get()
        )
    }

    single<FilterInteractor> {
        FilterInteractorImpl(
            filterRepository = get()
        )
    }
    single<ChooseCountryInteractor> {
        ChooseCountryInteractorImpl(
            repository = get()
        )
    }
    single<ChooseRegionInteractor> {
        ChooseRegionsInteractorImpl(
            repository = get()
        )
    }
    single<ChooseIndustryInteractor> {
        ChooseIndustryInteractorImpl(
            repository = get()
        )
    }
}