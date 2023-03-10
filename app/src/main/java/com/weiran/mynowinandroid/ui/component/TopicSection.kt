package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.weiran.mynowinandroid.store.data.model.TopicItem
import com.weiran.mynowinandroid.ui.pages.foryou.ForYouAction
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.ui.theme.Material

@Composable
fun TopicSection(
    modifier: Modifier = Modifier,
    topicItems: List<TopicItem>,
    forYouAction: (action: ForYouAction) -> Unit
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
                    imageUrl = it.imageUrl,
                    topicIcon = it.icon,
                    onCheckedChange = { forYouAction.invoke(ForYouAction.TopicSelected(it.id)) }
                )
            }
        }
    }
}