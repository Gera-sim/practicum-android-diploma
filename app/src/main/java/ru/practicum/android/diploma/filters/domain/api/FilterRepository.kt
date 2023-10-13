package ru.practicum.android.diploma.filters.domain.api

interface FilterRepository {
    fun saveFilters(
        location: String?,
        industry: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
        areaId: String?,
    )

    fun getLocation(): String
    fun getIndustry(): String?
    fun getExpectedSalary(): String?
    fun getRemoveNoSalary(): Boolean
    fun removeFilters()
    fun getAreaId(): String?
}