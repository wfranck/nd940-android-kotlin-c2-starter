package com.udacity.asteroidradar.utils

sealed class DataState {
    object Success   : DataState()
    object Error    : DataState()
    object Loading  : DataState()
}