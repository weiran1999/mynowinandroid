package com.weiran.mynowinandroid.store.repository

import com.weiran.mynowinandroid.store.data.model.News
import com.weiran.mynowinandroid.store.data.source.datasource.DataSource

interface NewsRepository {

    suspend fun getNews(): List<News>

    suspend fun updateIsMarkedById(newsId: String): Long

}

class NewsRepositoryImpl constructor(private val dataSource: DataSource) : NewsRepository {

    override suspend fun getNews(): List<News> = dataSource.getNews()

    override suspend fun updateIsMarkedById(newsId: String) = dataSource.updateIsMarkedById(newsId)
}