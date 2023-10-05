package me.gingerninja.lazylist.hijacker

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

/**
 * If [enabled] is `true`, it will override the internals of [LazyListState] so that the list would
 * not follow the first visible item in the list by scrolling to it when moved.
 */
class LazyListStateHijacker(
    private val listState: LazyListState,
    enabled: Boolean = true
) {
    private val scrollPositionField = listState.javaClass.getDeclaredField("scrollPosition").apply {
        isAccessible = true
    }

    private val scrollPositionObj = scrollPositionField.get(listState)

    private val lastKeyRemover: () -> Unit =
        scrollPositionField.type.getDeclaredField("lastKnownFirstItemKey").run {
            isAccessible = true

            fun() { set(scrollPositionObj, null) }
        }

    private val indexField = scrollPositionField.type.getDeclaredField("index\$delegate").apply {
        isAccessible = true
    }

    /**
     * Controls whether the hijack implementation is used or the default one.
     *
     * *Note: this is not backed by [androidx.compose.runtime.State]. You should not listen to the
     * changes of this in compose.*
     */
    var enabled: Boolean = enabled
        set(value) {
            if (field == value) {
                return
            }

            field = value

            setProps(value)
        }

    init {
        setProps(enabled)
    }

    private fun setProps(enable: Boolean) {
        val oldValue = indexField.get(scrollPositionObj).run {
            if (this is IntStateHijacker) {
                intValueDirect
            } else {
                listState.firstVisibleItemIndex
            }
        }

        val mutableIntState: MutableIntState = if (enable) {
            IntStateHijacker(
                state = mutableIntStateOf(oldValue),
                keyRemover = lastKeyRemover
            )
        } else {
            mutableIntStateOf(oldValue)
        }

        indexField.set(scrollPositionObj, mutableIntState)
    }
}

/**
 * Creates a [LazyListStateHijacker] that is remembered across compositions.
 *
 * Changes to the provided [listState] value will result in the state hijacker being recreated.
 *
 * Changes to the provided [enabled] value will **not** result in the state hijacker being recreated,
 * however the [LazyListStateHijacker.enabled] will be updated.
 *
 * @param listState the state that will be hijacked by [LazyListStateHijacker]
 * @param enabled the value for [LazyListStateHijacker.enabled]
 *
 * @return [LazyListStateHijacker] instance
 */
@Composable
fun rememberLazyListStateHijacker(
    listState: LazyListState,
    enabled: Boolean = true
): LazyListStateHijacker {
    return remember(listState) {
        LazyListStateHijacker(listState, enabled)
    }.apply {
        this.enabled = enabled
    }
}