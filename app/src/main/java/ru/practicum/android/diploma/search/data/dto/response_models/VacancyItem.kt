package ru.practicum.android.diploma.search.data.dto.response_models

import ru.practicum.android.diploma.vacancy.domain.models.Area
import ru.practicum.android.diploma.vacancy.domain.models.Employer

data class VacancyItem(
    val id: String,
    val area: Area,
    val name: String? = "",
    val city: String,
    val salary: Salary? = Salary(),
    val employer: Employer,
    val employerName: String,
    val employerLogoUrl: String?,
    val found: Int,
    val pages: Int,
    )