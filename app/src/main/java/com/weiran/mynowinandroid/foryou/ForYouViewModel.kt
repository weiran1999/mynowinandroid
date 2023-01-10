package com.weiran.mynowinandroid.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.NewsRepository
import com.weiran.mynowinandroid.repository.TopicRepository
import com.weiran.mynowinandroid.theme.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            checkTopicsSection()
            initTopicItems()
            loadNewsData()
            checkTopicSelected()
        }
    }

    private suspend fun loadNewsItems() =
        _forYouState.update { it.copy(newsItems = newsRepository.loadNewsItems()) }

    private fun checkTopicsSection() {
        if (!topicRepository.checkDoneShown()) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        }
    }

    private fun loadMarkedNews() {
        val markedNewsItems = _forYouState.value.newsItems.filter { newsItem -> newsItem.isMarked }
        _forYouState.update {
            it.copy(
                markedNewsItems = markedNewsItems,
                feedUIState = FeedUIState.Success
            )
        }
    }

    private suspend fun initTopicItems() =
        _forYouState.update { it.copy(topicItems = topicRepository.getTopicItems()) }

    private fun selectedTopic(topicId: String) {
        updateTopic(topicId)
        _forYouState.update { it.copy(feedUIState = FeedUIState.Loading) }
        viewModelScope.launch(ioDispatcher) {
            topicRepository.updateTopicSelected(topicId)
            checkTopicSelected()
        }
    }

    private fun updateTopic(topicId: String) =
        _forYouState.update { it.copy(topicItems = getTopicItemsByTopicId(topicId)) }

    private fun dispatchDone() {
        _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        topicRepository.updateDoneShown(false)
    }


    private suspend fun checkTopicSelected() {
        val isTopicSelected =
            topicRepository.checkTopicItemIsSelected(_forYouState.value.topicItems)
        if (!isTopicSelected) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.Shown) }
            topicRepository.updateDoneShown(true)
        }
        updateUIStateAndNewsItems(isTopicSelected)
    }

    private suspend fun updateUIStateAndNewsItems(isTopicSelected: Boolean) {
        _forYouState.update {
            it.copy(
                doneShownState = isTopicSelected,
                newsItems = loadNewsByChoiceTopics(),
                feedUIState = FeedUIState.Success
            )
        }
    }

    private suspend fun loadNewsByChoiceTopics() =
        withContext(ioDispatcher) {
            loadNewsItems()
            val selectedTopicIds = _forYouState.value.topicItems
                .filter { it.selected }
                .map { it.id }
            val newsItems = _forYouState.value.newsItems.filter {
                var flag = false
                it.topicItems.forEach { topicItem ->
                    if (selectedTopicIds.contains(topicItem.id)) flag = true
                }
                flag
            }
            newsItems
        }

    private fun getTopicItemsByTopicId(topicId: String) = _forYouState.value.topicItems.map {
        if (it.id == topicId) {
            val icon = if (it.selected) MyIcons.Add else MyIcons.Check
            it.copy(selected = !it.selected, icon = icon)
        } else {
            it
        }
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch(ioDispatcher) {
            newsRepository.changeNewsItemsById(newsId)
            loadNewsData()
        }
    }

    private suspend fun loadNewsData() {
        _forYouState.update { it.copy(feedUIState = FeedUIState.Loading) }
        loadNewsItems()
        loadMarkedNews()
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicSelected -> selectedTopic(action.topicId)
            is ForYouAction.DoneDispatch -> dispatchDone()
            is ForYouAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}