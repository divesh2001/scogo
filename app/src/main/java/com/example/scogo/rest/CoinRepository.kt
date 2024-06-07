package com.example.scogo.rest

import com.example.scogo.responseModel.CoinResponse

class CoinRepository {

    private val api = CoinPaprikaApi.RetrofitInstance.api

    suspend fun getCoins(): List<CoinResponse> {
        return api.getCoins()
    }
}