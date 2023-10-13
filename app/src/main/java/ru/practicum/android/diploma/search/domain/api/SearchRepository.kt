package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.utils.Resource
import ru.practicum.android.diploma.db.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails


interface SearchRepository {
    suspend fun searchVacancies(query: String): Flow<SearchVacancyResult>
    suspend fun loadVacancyDetails(vacancyId: String): Resource<VacancyDetails>
    fun getSimilarVacanciesById(vacancyId: String): Flow<Resource<List<Vacancy>>>
    fun loadVacanciesQueryMap(options: HashMap<String, String>): Flow<Resource<List<Vacancy>>>


}