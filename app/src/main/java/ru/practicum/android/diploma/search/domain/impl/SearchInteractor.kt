package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchInteractor(private val searchRepository: SearchRepository) {
    suspend fun execute(query: String): Flow<SearchVacancyResult> {
        return searchRepository.searchVacancies(query)
    }
}