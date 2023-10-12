package ru.practicum.android.diploma.search.data.impl

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.AREA_ID_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.EXPECTED_SALARY_KEY
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl.Companion.NO_SALARY_KEY
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val converter: ModelConverter,
    private val sharedPreferences: SharedPreferences,
) : SearchRepository {
    override suspend fun searchVacancies(query: String): Flow<SearchVacancyResult> =
        flow {
            //val queryParams = (mapOf("text" to "${query}", "per_page" to "50"))
            val queryParams = requestMaker(query)
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(SearchVacancyResult.NoInternet)
                    return@flow
                }
                val response = apiService.searchVacancies(queryParams)

                if (response.items.isEmpty()) {
                    4
                    emit(SearchVacancyResult.EmptyResult)
                } else {
                    val convertedResponse = converter.convertVacanciesResponse(response)
                    emit(SearchVacancyResult.Success(convertedResponse))
                }
            } catch (e: Exception) {
                emit(SearchVacancyResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    private fun requestMaker(query: String): Map<String, String> {
        var params = (mapOf("text" to "${query}", "per_page" to "50"))
        val areaId = sharedPreferences.getString(AREA_ID_KEY, null)
        if (!areaId.isNullOrEmpty()) {
            params += Pair("area", areaId)
        }
        val salary = sharedPreferences.getString(EXPECTED_SALARY_KEY, null)
        if (!salary.isNullOrEmpty()) {
            params += Pair("salary", salary)
        }
        val noSalary = sharedPreferences.getBoolean(NO_SALARY_KEY, false)
        params += Pair("only_with_salary", "$noSalary")
        return params
    }
}

