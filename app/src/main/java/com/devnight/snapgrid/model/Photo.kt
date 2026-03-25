package com.devnight.snapgrid.model

/**
 * Created by Efe Şen on 12,03,2026
 */

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val download_url: String
) {
    val thumbnailUrl: String
        get() = "https://picsum.photos/id/$id/300/300"
}