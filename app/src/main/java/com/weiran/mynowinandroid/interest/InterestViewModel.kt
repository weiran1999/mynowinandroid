package com.weiran.mynowinandroid.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _interestState = MutableStateFlow(InterestState())
    val interestState = _interestState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            _interestState.update { it.copy(topicItems = topicRepository.getTopicItems()) }
        }
    }

    fun observeData() {
        viewModelScope.launch { _interestState.update { it.copy(topicItems = topicRepository.getTopicItems()) } }
    }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch { topicRepository.updateTopicSelected(topicId) }
        topicRepository.checkTopicItemIsSelected()
        _interestState.update { it.copy(topicItems = topicRepository.topicItems) }
    }

    fun dispatchAction(action: InterestAction) {
        when (action) {
            is InterestAction.TopicSelected -> selectedTopic(action.topicId)
        }
    }

}