package com.example.scogo.rest

import com.example.scogo.responseModel.CoinResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class CoinPaprikaApi {

    interface CoinPaprikaApi {
        @GET("/v1/coins")
        suspend fun getCoins(): List<CoinResponse>
    }

    object RetrofitInstance {
        private const val BASE_URL = "https://api.coinpaprika.com/"

        val api: CoinPaprikaApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinPaprikaApi::class.java)
        }
    }
}