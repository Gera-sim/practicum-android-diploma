package ru.practicum.android.diploma.filters.data.converter

import ru.practicum.android.diploma.filters.data.dto.models.AreasDTO
import ru.practicum.android.diploma.filters.data.dto.models.IndustryDTO
import ru.practicum.android.diploma.filters.domain.models.Industry
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class FilterModelConverter {

    fun industryDTOListToIndustryList(list: List<IndustryDTO>): List<Industry> {
        return list.flatMap { it.industries ?: emptyList() }
            .map {
                Industry(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    industries = emptyList()
                )
            }
            .sortedBy { it.name }
    }

    fun regionDTOListToAreaList(list: List<AreasDTO>): List<Area> {
        val resultList = list.flatMap { it.areas }.map {
            Area(
                id = it.id ?: "",
                name = it.name ?: "",
                parentId = it.parentId ?: ""
            )
        }.sortedBy { it.id }
        return (resultList + list.flatMap { it.areas }.flatMap { it.areas }).sortedBy { it.id }
            .filterNot { it.parentId == "1001" }
    }
}
