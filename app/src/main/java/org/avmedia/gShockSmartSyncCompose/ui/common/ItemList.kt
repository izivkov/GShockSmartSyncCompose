package org.avmedia.gShockSmartSyncCompose.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> ItemList(
    items: List<T>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // .verticalScroll(rememberScrollState()) // Add scroll behavior
    ) {
        items.forEach { item ->
            ItemView {}
        }
    }
}