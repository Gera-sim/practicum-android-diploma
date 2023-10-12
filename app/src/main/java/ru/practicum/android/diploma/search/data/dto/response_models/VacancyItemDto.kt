package ru.practicum.android.diploma.search.data.dto.response_models

data class VacancyItemDto(
    val id: String,
    val area: Area?,
    val name: String? = "",
    val salary: Salary? = Salary(),
    val employer: Employer?,
)

