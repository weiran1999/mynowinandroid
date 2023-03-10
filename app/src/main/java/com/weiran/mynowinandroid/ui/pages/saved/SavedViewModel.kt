package com.weiran.mynowinandroid.ui.pages.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.domain.NewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavedViewModel constructor(private val newsUseCase: NewsUseCase) : ViewModel() {

    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()

    fun fetchData() {
        updateFeedData()
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch { newsUseCase.changeNewsItemsById(newsId) }
        updateFeedData()
    }

    private fun updateFeedData() {
        _savedState.update {
            it.copy(
                markedNewsItems = newsUseCase.loadMarkedNewsItems(),
                savedUIState = newsUseCase.updateSavedUIState()
            )
        }
    }

    fun onAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}