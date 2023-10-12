package ru.practicum.android.diploma.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchVacancyResult

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _searchVacancyResult: MutableLiveData<SearchVacancyResult> = MutableLiveData()
    val searchVacancyResult: LiveData<SearchVacancyResult> = _searchVacancyResult

    fun searchVacancies(query: String) {
        viewModelScope.launch {
            searchInteractor.execute(query).collect { result ->
                _searchVacancyResult.value = result
            }
        }
    }
}