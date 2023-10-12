package ru.practicum.android.diploma.core.di

import org.koin.dsl.module
import ru.practicum.android.diploma.filters.data.repository.ChooseCountryRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.ChooseRegionRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository

val repositoryModule = module {

    factory<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<FilterRepository> { FilterRepositoryImpl(get()) }
    single<ChooseCountryRepository> { ChooseCountryRepositoryImpl(get(), get()) }
    single<ChooseRegionRepository> { ChooseRegionRepositoryImpl(get(), get()) }
}