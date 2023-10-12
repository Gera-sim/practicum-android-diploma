package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
) : FilterInteractor {
    override fun saveFilters(
        location: String?,
        industry: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        filterRepository.saveFilters(
            location,
            industry,
            expectedSalary,
            removeNoSalary
        )
    }

    override fun getLocation(): String {
        return filterRepository.getLocation()
    }

    override fun getIndustry(): String? {
        return filterRepository.getIndustry()
    }

    override fun getExpectedSalary(): String? {
        return filterRepository.getExpectedSalary()
    }

    override fun getRemoveNoSalary(): Boolean {
        return filterRepository.getRemoveNoSalary()
    }

    override fun removeFilters() {
        filterRepository.removeFilters()
    }
}