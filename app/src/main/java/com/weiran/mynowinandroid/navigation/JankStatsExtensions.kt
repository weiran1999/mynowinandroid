package com.weiran.mynowinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.metrics.performance.PerformanceMetricsState
import androidx.metrics.performance.PerformanceMetricsState.Holder
import kotlinx.coroutines.CoroutineScope

/**
 * Retrieves [PerformanceMetricsState.Holder] from current [LocalView] and
 * remembers it until the View changes.
 * @see PerformanceMetricsState.getHolderForHierarchy
 */
@Composable
fun rememberMetricsStateHolder(): Holder {
    val localView = LocalView.current

    return remember(localView) {
        PerformanceMetricsState.getHolderForHierarchy(localView)
    }
}

/**
 * Convenience function to work with [PerformanceMetricsState] state. The side effect is
 * re-launched if any of the [keys] value is not equal to the previous composition.
 * @see TrackDisposableJank if you need to work with DisposableEffect to cleanup added state.
 */
@Composable
fun TrackJank(
    vararg keys: Any?,
    reportMetric: suspend CoroutineScope.(state: Holder) -> Unit
) {
    val metrics = rememberMetricsStateHolder()
    LaunchedEffect(metrics, *keys) {
        reportMetric(metrics)
    }
}

/**
 * Convenience function to work with [PerformanceMetricsState] state that needs to be cleaned up.
 * The side effect is re-launched if any of the [keys] value is not equal to the previous composition.
 */
@Composable
fun TrackDisposableJank(
    vararg keys: Any?,
    reportMetric: DisposableEffectScope.(state: Holder) -> DisposableEffectResult
) {
    val metrics = rememberMetricsStateHolder()
    DisposableEffect(metrics, *keys) {
        reportMetric(this, metrics)
    }
}