package com.devnight.snapgrid.network

import com.devnight.snapgrid.model.Photo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Efe Şen on 12,03, 2026
 */
interface PicsumApi {
    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<Photo>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://picsum.photos/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val picsumApi = retrofit.create(PicsumApi::class.java)