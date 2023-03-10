package com.weiran.mynowinandroid.store.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 1,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "content")
    var content: String = "",

    @ColumnInfo(name = "isMarked")
    var isMarked: Boolean = false,

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "headerImageUrl")
    var headerImageUrl: String = "",
)