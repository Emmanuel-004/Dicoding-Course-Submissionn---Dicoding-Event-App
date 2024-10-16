package com.event.dicodingeventapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.response.DetailEventResponse
import com.event.dicodingeventapp.data.response.Event
import com.event.dicodingeventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getDetailEvents(eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvents(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}