package com.mediaservice.infrastructure

data class PageRequest(
    val pageNum: Int,
    val pageSize: Int
) {
    companion object {
        fun of(pageNum: Int, pageSize: Int) = PageRequest(
            pageNum = pageNum,
            pageSize = pageSize
        )
    }
}
