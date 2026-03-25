package com.devnight.snapgrid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.devnight.snapgrid.network.PhotoPagingSource
import com.devnight.snapgrid.network.PicsumApi
import com.devnight.snapgrid.network.retrofit

/**
 * Created by Efe Şen on 12,03, 2026
 */
class PhotoViewModel : ViewModel() {
    private val api = retrofit.create(PicsumApi::class.java)

    val photos = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 40,
            enablePlaceholders = true
        )) {
        PhotoPagingSource(api)
    }.flow.cachedIn(viewModelScope)
}