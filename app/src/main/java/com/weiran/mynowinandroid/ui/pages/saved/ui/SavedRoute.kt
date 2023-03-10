package com.weiran.mynowinandroid.ui.pages.saved.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.weiran.mynowinandroid.ui.pages.saved.SavedViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun SavedRoute(viewModel: SavedViewModel = getViewModel()) {
    rememberCoroutineScope().launch { viewModel.fetchData() }
    SavedScreen(viewModel)
}