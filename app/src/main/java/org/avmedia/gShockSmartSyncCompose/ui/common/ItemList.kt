package org.avmedia.gShockSmartSyncCompose.ui.common

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun <T> ItemList(
    items: List<T>,
) {
    LazyColumn {
        items(items) {
            ItemView {}
        }
    }
}