package com.example.scogo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scogo.responseModel.CoinResponse
import com.example.scogo.rest.CoinRepository
import kotlinx.coroutines.launch

class CoinViewModel : ViewModel(){
    private val repository = CoinRepository()
    private val _coins = MutableLiveData<List<CoinResponse>>()
    val coins: LiveData<List<CoinResponse>> = _coins

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchCoins()
    }

    fun fetchCoins() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val coins = repository.getCoins()
                _coins.value = coins
            } catch (e: Exception) {
                //->> Error Handler
            } finally {
                _isLoading.value = false
            }
        }
    }
}
