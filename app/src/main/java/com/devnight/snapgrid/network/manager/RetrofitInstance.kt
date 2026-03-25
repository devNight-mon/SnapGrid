package com.devnight.snapgrid.network.manager

import com.devnight.snapgrid.SnapGridApp
import com.devnight.snapgrid.network.PicsumApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Created by Efe Şen on 12,03, 2026
 */
object RetrofitInstance {

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(File(SnapGridApp.instance.cacheDir, "http_cache"), 50L * 1024L * 1024L))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://picsum.photos/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PicsumApi by lazy {
        retrofit.create(PicsumApi::class.java)
    }
}