package ru.practicum.android.diploma.search.domain.models

data class Vacancy(
    val id: String,
    val logoUrl: String = "",
    val title: String? = "",
    val company: String = "",
    val salary: String? = "",
)