package com.devnight.snapgrid.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devnight.snapgrid.model.Photo

/**
 * Created by Efe Şen on 12,03,2026
 */
class PhotoPagingSource(
    private val api: PicsumApi
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        Log.d("API_LOG", "Loading page $page")

        return try {
            val response = api.getPhotos(page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}