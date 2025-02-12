package com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avito.tech.avito_tech_winter_2025.repository.AvitoRepository
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data.Result
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApiTracksViewModel @Inject constructor(private val avitoRepository: AvitoRepository) :
    ViewModel() {
    val tracksLiveData: LiveData<Result>
        get() = _tracksLiveData
    private val _tracksLiveData: MutableLiveData<Result> = MutableLiveData()
    var query: String = ""
        set(value) {
            field = value
            request(value)
        }

    init {
        _tracksLiveData.value = Result(Status.LOADING)
        query = ""
    }
    fun refresh() {
        request(query)
    }
    private fun request(query: String){
        _tracksLiveData.value = Result(Status.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _tracksLiveData.postValue(avitoRepository.getTracks())
            } else {
                _tracksLiveData.postValue(avitoRepository.getTracksQuery(query))
            }
        }
    }
}