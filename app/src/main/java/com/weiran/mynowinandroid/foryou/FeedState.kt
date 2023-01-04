package com.weiran.mynowinandroid.foryou

import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem

data class FeedState(
    val topicItems: List<TopicItem> = listOf(),
    val doneButtonState: Boolean = false,
    val sectionUiState: SectionUiState = SectionUiState.Shown,
    val newsItems: List<NewsItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)

sealed class SectionUiState {
    object Shown : SectionUiState()
    object NotShown : SectionUiState()
}

sealed class FeedUIState {
    object Loading : FeedUIState()
    object Success : FeedUIState()
}