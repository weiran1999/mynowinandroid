package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.ui.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.ui.component.NewsCard
import com.weiran.mynowinandroid.ui.component.TopicSection
import com.weiran.mynowinandroid.ui.theme.Colors.WHITE_GRADIENTS
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.viewmodel.FeedViewModel
import com.weiran.mynowinandroid.viewmodel.FeedUIState
import com.weiran.mynowinandroid.viewmodel.SectionUiState
import com.weiran.mynowinandroid.viewmodel.FeedAction

@Composable
fun ForYouScreen() {
    val feedViewModel: FeedViewModel = viewModel()
    val feedState = feedViewModel.feedState.collectAsState().value

    LazyColumn {
        item {
            when (feedState.sectionUiState) {
                is SectionUiState.Shown -> ShownContent(
                    feedState.topicItems,
                    feedViewModel::dispatchAction,
                    feedState.doneButtonState
                )

                is SectionUiState.NotShown -> Unit
            }
            feedState.newsItems.forEach {
                NewsCard(
                    onToggleMark = {},
                    onClick = {},
                    isMarked = false,
                    newsItem = it,
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = WHITE_GRADIENTS
                            )
                        )
                        .padding(Dimensions.standardSpacing)
                )
            }
        }
    }
    MyOverlayLoadingWheel(isFeedLoading = feedState.feedUIState is FeedUIState.Loading)
}

@Composable
private fun ShownContent(
    topicItems: List<TopicItem>,
    dispatchAction: (action: FeedAction) -> Unit,
    doneButtonState: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(Dimensions.standardSpacing))
        Text(
            text = stringResource(R.string.for_you_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(Dimensions.standardSpacing))
        Text(
            text = stringResource(R.string.for_you_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.standardPadding),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        TopicSection(
            topicItems = topicItems,
            dispatchAction = dispatchAction,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = doneButtonState,
                onClick = { dispatchAction.invoke(FeedAction.DoneDispatch) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.buttonPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
            ) {
                Text(
                    text = stringResource(R.string.done)
                )
            }
        }

    }
}