package ru.practicum.android.diploma.filters.domain.models


class FiltersState(
    var location: String?,
    var industry: String?,
    var lowestSalary: String?,
    var removeNoSalary: Boolean,
)