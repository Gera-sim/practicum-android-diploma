package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult

class ChooseIndustryInteractorImpl(private val repository: ChooseIndustryRepository) :
    ChooseIndustryInteractor {
    override suspend fun getIndustry(): Flow<ChooseIndustryResult> {
        return repository.getIndustry()
    }

}