package ru.practicum.android.diploma.filters.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.models.ChooseIndustryResult
import ru.practicum.android.diploma.filters.domain.models.ChooseRegionsResult
import ru.practicum.android.diploma.filters.domain.models.ChooseResult
import ru.practicum.android.diploma.filters.domain.models.FiltersState

class FiltersViewModel(
    private val filterInteractor: FilterInteractor,
    private val chooseCountryInteractor: ChooseCountryInteractor,
    private val chooseRegionInteractor: ChooseRegionInteractor,
    private val chooseIndustryInteractor: ChooseIndustryInteractor,
) : ViewModel() {
    private val _stateLiveData = MutableLiveData<FiltersState>()
    private var emptyFilters = FiltersState(null, null, null, false, null)
    fun observeState(): LiveData<FiltersState> = _stateLiveData

    private val _chooseResult: MutableLiveData<ChooseResult> = MutableLiveData()
    val chooseResult: LiveData<ChooseResult> = _chooseResult
    private val _chooseRegionResult: MutableLiveData<ChooseRegionsResult> = MutableLiveData()
    val chooseRegionResult: LiveData<ChooseRegionsResult> = _chooseRegionResult
    private val _chooseIndustryResult: MutableLiveData<ChooseIndustryResult> = MutableLiveData()
    val chooseIndustryResult: LiveData<ChooseIndustryResult> = _chooseIndustryResult

    init {
        loadFilters()
    }

    fun getCountry() {
        viewModelScope.launch {
            chooseCountryInteractor.execute().collect { result ->
                _chooseResult.postValue(result)
            }
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            chooseRegionInteractor.getAreas().collect { result ->
                _chooseRegionResult.postValue(result)
            }
        }
    }

    fun getIndustry() {
        viewModelScope.launch {
            chooseIndustryInteractor.getIndustry().collect { result ->
                _chooseIndustryResult.postValue(result)
            }
        }
    }

    private fun loadFilters() {
        emptyFilters.location = filterInteractor.getLocation()
        emptyFilters.areaId = filterInteractor.getAreaId()
        emptyFilters.industry = filterInteractor.getIndustry()
        emptyFilters.lowestSalary = filterInteractor.getExpectedSalary()
        emptyFilters.removeNoSalary = filterInteractor.getRemoveNoSalary()
        _stateLiveData.postValue(emptyFilters)
    }

    fun saveFilters(
        location: String?,
        industry: String?,
        expectedSalary: String?,
        removeNoSalary: Boolean,
    ) {
        filterInteractor.saveFilters(
            location,
            industry,
            expectedSalary,
            removeNoSalary,
            emptyFilters.areaId,
        )
    }

    fun removeFilters() {
        filterInteractor.removeFilters()
        loadFilters()
    }

    fun getLocation(location: String?, areaId: String?) {
        emptyFilters.location = location
        emptyFilters.areaId = areaId
        _stateLiveData.postValue(emptyFilters)
    }
}