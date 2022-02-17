package com.mediaservice.infrastructure

data class Page<T>(
    val contentList: List<T>,
    val totalPageNum: Int,
    val pageNum: Int
) {
    companion object {
        fun <T> of(
            contentList: List<T>,
            totalPageNum: Int,
            pageNum: Int
        ) = Page(
            contentList = contentList,
            totalPageNum = totalPageNum,
            pageNum = pageNum
        )
    }
}
