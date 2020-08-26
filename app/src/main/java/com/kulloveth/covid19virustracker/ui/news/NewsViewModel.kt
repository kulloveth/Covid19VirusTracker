package com.kulloveth.covid19virustracker.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kulloveth.covid19virustracker.data.Repository
import com.kulloveth.covid19virustracker.data.db.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * News ViewModel extending from the [BaseViewModel]
 * */
class NewsViewModel(private val repository: Repository) : ViewModel() {

    private val newCovidNewsLiveData = MutableLiveData<List<NewsEntity>>()

    private val _covidNewsLiveData = MutableLiveData<NewsEntity>()
    val covidNewsLiveData = _covidNewsLiveData

    init {
        repository.fetchNews()
        repository.infoNotify()
    }

    fun getNewCovidNews():LiveData<List<NewsEntity>>{
        viewModelScope.launch(Dispatchers.IO) {
            val status =  repository.fetchNewsFromRoom()
            newCovidNewsLiveData.postValue(status)
        }

        return newCovidNewsLiveData
    }

    fun passNewsDetail(newsEntity: NewsEntity){
        _covidNewsLiveData.value = newsEntity
    }


}