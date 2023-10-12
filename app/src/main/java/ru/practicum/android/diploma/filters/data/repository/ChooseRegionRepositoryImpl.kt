package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper

class ChooseRegionRepositoryImpl(
    private val apiService: ApiService,
    private val networkControl: ConnectivityHelper,
) : ChooseRegionRepository {
    override suspend fun getRegions(): Flow<ChooseRegionsResult> =
        flow {
            try {
                if (!networkControl.isInternetAvailable()) {
                    emit(ChooseRegionsResult.NoInternet)
                    return@flow
                }
                val response = apiService.getAreas()
                val list = response.flatMap { it.areas }
                val list2 = list.flatMap { it.areas }
                if (response.isEmpty()) {
                    emit(ChooseRegionsResult.EmptyResult)
                } else {
                    emit(ChooseRegionsResult.Success(list2))
                }
            } catch (e: Exception) {
                emit(ChooseRegionsResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}