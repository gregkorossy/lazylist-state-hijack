-keepclassmembers class androidx.compose.foundation.lazy.LazyListState {
    private androidx.compose.foundation.lazy.LazyListScrollPosition scrollPosition;
}

-keepclassmembers class androidx.compose.foundation.lazy.LazyListScrollPosition {
    private androidx.compose.runtime.MutableIntState index$delegate;
    private java.lang.Object lastKnownFirstItemKey;
}