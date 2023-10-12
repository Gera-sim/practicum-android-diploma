package ru.practicum.android.diploma.filters.domain.models

sealed class ChooseIndustryResult {
    data class Success(val response: List<Industry>) : ChooseIndustryResult()
    data class Error(val exception: Exception) : ChooseIndustryResult()
    object NoInternet : ChooseIndustryResult()
    object EmptyResult : ChooseIndustryResult()
}