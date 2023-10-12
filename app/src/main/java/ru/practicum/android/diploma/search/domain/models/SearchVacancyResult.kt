package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.search.data.dto.VacanciesResponse

sealed class SearchVacancyResult {
    data class Success(val response: VacanciesResponse) : SearchVacancyResult()
    data class Error(val exception: Exception) : SearchVacancyResult()
    object NoInternet : SearchVacancyResult()
    object EmptyResult : SearchVacancyResult()
}
