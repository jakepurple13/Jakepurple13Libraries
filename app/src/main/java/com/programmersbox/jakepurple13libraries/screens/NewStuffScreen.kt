package com.programmersbox.jakepurple13libraries.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import com.programmersbox.jakepurple13libraries.ui.theme.Jakepurple13LibrariesTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun NewScreen() {
    ScaffoldTop(screen = Screen.NewScreen) { padding ->
        LazyColumn(
            contentPadding = padding
        ) {
            item {
                FlowRow {
                    repeat(50) {
                        Icon(Icons.Default.Add, null)
                    }
                }
            }

            item {
                Box(Modifier.width(30.dp)) {
                    Text(
                        "Hello World",
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(animationMode = MarqueeAnimationMode.Immediately)
                    )
                }
            }

            item {
                PlainTooltipBox(
                    tooltip = { Text("Add to favorites") }
                ) {
                    IconButton(
                        onClick = { /* Icon button's click event */ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen() {
    ScaffoldTop(Screen.DateScreen) { padding ->
        Column(Modifier.padding(padding)) {
            val state = rememberDatePickerState()
            DatePicker(state)
            Text("Years Range: ${state.yearsRange} | Date: ${state.selectedDateMillis}")
            var showDatePickerDialog by remember { mutableStateOf(false) }
            if (showDatePickerDialog) {
                AlertDialog(
                    onDismissRequest = { showDatePickerDialog = false },
                    text = { DatePicker(state) },
                    confirmButton = { TextButton(onClick = { showDatePickerDialog = false }) { Text("Done") } }
                )
            }
            Button(onClick = { showDatePickerDialog = true }) {
                Text("Show Date Picker in Dialog")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen() {
    var dockedOrNot by remember { mutableStateOf(false) }

    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var active1 by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun closeSearchBar() {
        focusManager.clearFocus()
        active = false
        active1 = false
    }

    val searchBarContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                @OptIn(ExperimentalMaterial3Api::class)
                ListItem(
                    headlineText = { Text(resultText) },
                    supportingText = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier.clickable {
                        text = resultText
                        closeSearchBar()
                    }
                )
                if (idx != 3) {
                    Divider()
                }
            }
        }
    }

    ScaffoldTop(
        Screen.SearchBarScreen,
        topBarActions = {
            Text("Docked/Not")
            Spacer(Modifier.width(2.dp))
            Switch(
                checked = dockedOrNot,
                onCheckedChange = { dockedOrNot = it }
            )
        },
        bottomBar = {
            DockedSearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = { text = it },
                onSearch = { closeSearchBar() },
                active = active1,
                onActiveChange = {
                    active1 = it
                    if (!active1) focusManager.clearFocus()
                },
                placeholder = { Text("Hinted search text") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            ) {
                searchBarContent()
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // This is okay to put first because search bars by default have zIndex = 1f.
            when (dockedOrNot) {
                true -> {
                    SearchBar(
                        modifier = Modifier.align(Alignment.TopCenter),
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = { closeSearchBar() },
                        active = active,
                        onActiveChange = {
                            active = it
                            if (!active) focusManager.clearFocus()
                        },
                        placeholder = { Text("Hinted search text") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                    ) {
                        searchBarContent()
                    }
                }

                false -> {
                    DockedSearchBar(
                        modifier = Modifier.align(Alignment.TopCenter),
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = { closeSearchBar() },
                        active = active,
                        onActiveChange = {
                            active = it
                            if (!active) focusManager.clearFocus()
                        },
                        placeholder = { Text("Hinted search text") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                    ) {
                        searchBarContent()
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val list = List(100) { "Text $it" }
                items(count = list.size) {
                    Text(list[it], Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun DatePreview() {
    Jakepurple13LibrariesTheme(true) {
        DateScreen()
    }
}

@Preview
@Composable
fun NewStuffPreview() {
    Jakepurple13LibrariesTheme(true) {
        NewScreen()
    }
}

@Preview
@Composable
fun SearchPreview() {
    Jakepurple13LibrariesTheme(true) {
        SearchBarScreen()
    }
}