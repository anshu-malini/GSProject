package com.am.gsproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.gsproject.data.db.entities.ApodEntity
import com.am.gsproject.data.db.repository.ApodRepository
import com.am.gsproject.utils.GENERAL_ERROR
import com.am.gsproject.utils.LOG_TAG_NAME
import com.am.gsproject.utils.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private var repository: ApodRepository,
    private var hasInternet: Boolean
) : ViewModel() {
    val apodData: MutableLiveData<NetworkResult<List<ApodEntity>>> = MutableLiveData()
    val favList: MutableLiveData<NetworkResult<List<ApodEntity>>> = MutableLiveData()

    fun getApods(apiKey: String, date: String) {
        apodData.postValue(NetworkResult.loading())
        viewModelScope.launch {
            try {
                apodData.postValue(repository.getApods(apiKey, date, hasInternet))
            } catch (ex: java.lang.Exception) {
                ex.localizedMessage?.let { Log.e(LOG_TAG_NAME, it) }
                apodData.postValue(NetworkResult.error(GENERAL_ERROR))
            }
        }
    }

    fun setApodFavStatus(apidId: Long, isFav: String) {
        favList.postValue(NetworkResult.loading())
        viewModelScope.launch {
            try {
                val result = repository.setApodIsFav(apidId, isFav)
                getApodsFavList()
                apodData.postValue(result)
                //favList.postValue(result)
            } catch (ex: java.lang.Exception) {
                ex.localizedMessage?.let { Log.e(LOG_TAG_NAME, it) }
                favList.postValue(NetworkResult.error(GENERAL_ERROR))
            }
        }
    }

    fun getApodsFavList() {
        favList.postValue(NetworkResult.loading())
        viewModelScope.launch {
            try {
                favList.postValue(repository.getApodByIsFav())
            } catch (ex: java.lang.Exception) {
                ex.localizedMessage?.let { Log.e(LOG_TAG_NAME, it) }
                favList.postValue(NetworkResult.error(GENERAL_ERROR))
            }
        }
    }
}
