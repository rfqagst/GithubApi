package com.example.githubapisub.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapisub.data.response.ItemsItem
import com.example.githubapisub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragViewModel : ViewModel() {

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private val _userFollowers = MutableLiveData<List<ItemsItem>>()
    val userFollowers: LiveData<List<ItemsItem>> = _userFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getFollowing(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _userFollowing.value = response.body()
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = true
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getFollowers(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _userFollowers.value = response.body()
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
    }
}


