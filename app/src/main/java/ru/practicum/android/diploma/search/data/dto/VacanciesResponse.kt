package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.data.dto.response_models.VacancyItemDto


data class VacanciesResponse(
    val found: Int,
    val items: List<VacancyItemDto>,
    val page: Int,
    val pages: Int,
    val per_page: Int,
) {
    companion object {
        val empty = VacanciesResponse(0, emptyList(), 0, 0, 0)
    }
}