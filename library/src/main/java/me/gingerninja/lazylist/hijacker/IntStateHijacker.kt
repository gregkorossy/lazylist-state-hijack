package me.gingerninja.lazylist.hijacker

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableIntState

/**
 * Listens to the changes in [state] and calls [keyRemover] when something is retrieving [value] or
 * [intValue], which is _likely_ to happen before the call to
 * `LazyListScrollPosition.updateScrollPositionIfTheFirstItemWasMoved`.
 */
@SuppressLint("AutoboxingStateValueProperty")
internal class IntStateHijacker(
    private val state: MutableIntState,
    private val keyRemover: () -> Unit
) : MutableIntState by state {
    override var intValue: Int
        get() {
            keyRemover()
            return state.intValue
        }
        set(value) {
            state.intValue = value
        }

    override var value: Int
        get() {
            keyRemover()
            return state.value
        }
        set(value) {
            state.value = value
        }

    /**
     * Provides direct access to the [state] object's `intValue` property without triggering the
     * [keyRemover].
     */
    val intValueDirect: Int get() = state.intValue
}