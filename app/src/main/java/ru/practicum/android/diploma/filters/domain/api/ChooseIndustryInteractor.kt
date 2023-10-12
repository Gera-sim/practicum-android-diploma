package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult

interface ChooseIndustryInteractor {
    suspend fun getIndustry(): Flow<ChooseIndustryResult>
}