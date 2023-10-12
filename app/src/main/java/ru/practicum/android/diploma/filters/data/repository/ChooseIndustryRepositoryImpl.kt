package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper

class ChooseIndustryRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
    private val filterModelConverter: FilterModelConverter,
) : ChooseIndustryRepository {
    override suspend fun getIndustry(): Flow<ChooseIndustryResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(ChooseIndustryResult.NoInternet)
                    return@flow
                }
                val response = apiService.getIndustries()
                val list = filterModelConverter.industryDTOListToIndustryList(response)
                if (list.isEmpty()) {
                    emit(ChooseIndustryResult.EmptyResult)
                } else {
                    emit(ChooseIndustryResult.Success(list))
                }
            } catch (e: Exception) {
                emit(ChooseIndustryResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

}