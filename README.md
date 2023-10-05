# LazyList hijacker

![GitHub](https://img.shields.io/github/license/gregkorossy/lazylist-state-hijack)
![Maven Central](https://img.shields.io/maven-central/v/me.gingerninja.lazylist/hijacker)
![GitHub Repo stars](https://img.shields.io/github/stars/gregkorossy/lazylist-state-hijack)

`LazyColumn` is a very useful component of the compose ecosystem. It's the somewhat equivalent of `RecyclerView` from the view / XML  world. However, it has a huge problem when it comes to changing the order of the list items, which [was reported a few years ago](https://issuetracker.google.com/issues/209652366) first and it's yet to be fixed. This library is supposed to provide a quickfix for this until the official implementation gets fixed.

**The solution works with any `LazyList` implementation, such as `LazyColumn` and `LazyRow`.**

## Install

Add the following dependency to your module `build.gradle.kts` file:

```kotlin 
implementation("me.gingerninja.lazylist:hijacker:1.0.0")
```

If you are using Gradle instead of Kotlin DSL, add it to your module `build.gradle` file:
```gradle 
implementation "me.gingerninja.lazylist:hijacker:1.0.0"
```

## Usage

You can get the hijacker instance for the `LazyListState` by calling
```kotlin
val hijacker = rememberLazyListStateHijacker(listState = listState)
```

or if you don't want to auto-enable it just yet (for whatever reason):
```kotlin
val hijacker = rememberLazyListStateHijacker(listState = listState, enabled = false)
```

The returned instance can be controlled by its `enabled` flag:

```kotlin
hijacker.enabled = true // enable the hijacked implementation

hijacker.enabled = false // disable the hijacked implementation
```

### Example

```kotlin
// create the LazyListState the usual way
val listState = rememberLazyListState()

// the magic happens here
rememberLazyListStateHijacker(listState = listState, enabled = true)

// use the LazyColumn as usual
LazyColumn(
    state = listState
) {
    // ...
}
```

## Compatibility

This lib was tested against the latest versions of the `compose-foundation` artifact, namely `1.5.3` and `1.6.0-alpha07`, but should work with earlier versions as well.

> This library works only on the JVM as it relies on Java Reflection.