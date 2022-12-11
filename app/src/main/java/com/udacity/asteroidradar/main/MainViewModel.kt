package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.getInstance
import com.udacity.asteroidradar.repository.AstroRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var database: AsteroidDatabase
    private var myRepo: AstroRepo
    private var _pictOfTheDay: LiveData<PictureOfDay>
    val astroList: MediatorLiveData<List<Asteroid>> = MediatorLiveData()

    val pictOfTheDay:LiveData<PictureOfDay>
    get() = _pictOfTheDay
    private val _selectedAstro = MutableLiveData<Asteroid>()
    val selectedAstro: LiveData<Asteroid>
        get() = _selectedAstro

    init {
        database = getInstance(application)
        myRepo = AstroRepo(database)
        _pictOfTheDay = myRepo.pictureOfDay
        getAsteroidData()
    }

    private fun getAsteroidData() {
        viewModelScope.launch {
            myRepo.refreshAsteroidList()
            myRepo.getPictureOfTheDate()
            astroList.addSource(myRepo.asteroidList) {
                astroList.value = it
            }
        }
    }

    fun menuItemClicked(typ: String) {
        removeSource()
        var data = when (typ) {
            "today" -> myRepo.todayAsteroidList
            else -> myRepo.asteroidList
        }
        astroList.addSource(data) {
            astroList.value = it
        }

    }

    private fun removeSource() {
        astroList.removeSource(myRepo.asteroidList)
        astroList.removeSource(myRepo.todayAsteroidList)
    }

    fun showAstroidDetail(asteroid: Asteroid) {
        _selectedAstro.value = asteroid
    }

    fun showAstroidDetailCompleted() {
        _selectedAstro.value = null
    }



}