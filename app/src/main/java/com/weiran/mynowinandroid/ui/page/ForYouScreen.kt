package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.ui.component.TopicSection
import com.weiran.mynowinandroid.viewmodel.TopicAction
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.viewmodel.SectionUiState
import com.weiran.mynowinandroid.viewmodel.TopicViewModel

@Composable
fun ForYouScreen(
    viewModel: TopicViewModel = viewModel()
) {
    val state = viewModel.topicState.collectAsState()
    val topicItems = state.value.topicItems
    val doneButtonState = state.value.doneButtonState
    val sectionUiState = state.value.sectionUiState
    val dispatchAction = viewModel::dispatchAction

    when (sectionUiState) {
        is SectionUiState.Shown -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            TopicSection(topicItems = topicItems, dispatchAction = dispatchAction)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = doneButtonState,
                    onClick = { dispatchAction.invoke(TopicAction.DoneButtonClickAction) },
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

        is SectionUiState.NotShown -> Unit
    }

}