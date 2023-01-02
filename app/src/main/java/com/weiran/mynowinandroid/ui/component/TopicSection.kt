package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.ui.theme.Material
import com.weiran.mynowinandroid.viewmodel.NewsAction
import com.weiran.mynowinandroid.viewmodel.TopicAction

@Composable
fun TopicSection(
    modifier: Modifier = Modifier,
    topicItems: List<TopicItem>,
    topicDispatchAction: (action: TopicAction) -> Unit,
    newsDispatchAction: (action: NewsAction) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(Material.cellRows),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.standardSpacing),
        verticalArrangement = Arrangement.spacedBy(Dimensions.standardSpacing),
        contentPadding = PaddingValues(Dimensions.standardPadding),
        modifier = modifier
            .heightIn(max = Dimensions.heightInMax)
            .fillMaxWidth()
    ) {
        topicItems.forEach {
            item {
                TopicItem(
                    name = it.name,
                    selected = it.selected,
                    topicIcon = it.icon,
                    onCheckedChange = {
                        topicDispatchAction.invoke(TopicAction.TopicSelected(it.id))
                        newsDispatchAction.invoke(NewsAction.TopicNewsSelected)
                    },
                )
            }
        }
    }
}