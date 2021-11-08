package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.database.RepositoryAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val apiKey: String, application: Application) : AndroidViewModel(application) {

    private var _statusLoad = MutableLiveData<Boolean>()
    val statusLoad: LiveData<Boolean> = _statusLoad

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroid: LiveData<List<Asteroid>> = _asteroids

    private val repositoryAsteroid: RepositoryAsteroid by lazy {
        val database = getDatabase(getApplication())
        RepositoryAsteroid(database)
    }

    init {
        getPictureOfDay()
        getAsteroids()
    }

    //-
    private fun getPictureOfDay() {
        viewModelScope.launch {
            repositoryAsteroid.pictureOfDay.map {
                it.asDomainModel()
            }.collect {
                if (it != null) {
                    _pictureOfDay.postValue(it)
                } else {
                    viewModelScope.launch {
                        repositoryAsteroid.fetchPictureOfDay(apiKey)
                    }
                }
            }
        }
    }

    //-
    @ExperimentalCoroutinesApi
    fun getAsteroids() {
        viewModelScope.launch {
            repositoryAsteroid.asteroids.map {
                it.asDomainModel()
            }.collect { if (it.isNotEmpty()) _asteroids.postValue(it) else fetchAsteroids() }
        }
    }

    //-
    @ExperimentalCoroutinesApi
    private fun fetchAsteroids() {
        viewModelScope.launch {
            repositoryAsteroid.refreshAsteroids(apiKey).collect { state ->
                when(state) {
                    DataState.Loading   -> _statusLoad.value = true
                    DataState.Error     -> _statusLoad.value = false
                    DataState.Success    -> _statusLoad.value = false
                    else -> _statusLoad.value = false
                }
            }
        }
    }

    //-
    fun getAsteroidsUntil() {
        viewModelScope.launch {
            repositoryAsteroid.asteroidsUntil.map {
                it.asDomainModel()
            }.collect { _asteroids.value = it }
        }
    }
    //-
    fun getAsteroidsToday() {
        viewModelScope.launch {
            repositoryAsteroid.asteroidsToday.map {
                it.asDomainModel()
            }.collect { _asteroids.value = it }
        }
    }
    //-
    class Factory(private val apiKey: String, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(apiKey, application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}