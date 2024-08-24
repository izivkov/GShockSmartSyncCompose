package org.avmedia.gShockSmartSyncCompose.ui.settings

import AppText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Locale(
    timeFormat: String,
    dateFormat: String,
    selectedLanguage: String,
    onTimeFormatChange: (String) -> Unit,
    onDateFormatChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
) {

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Time Format Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    text = stringResource(id = R.string.time_format),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically // Aligns children vertically in the center
                ) {
                    RadioButton(
                        selected = timeFormat == "12h",
                        onClick = { onTimeFormatChange("12h") }
                    )
                    AppText(text = stringResource(id = R.string._12h))
                    Spacer(modifier = Modifier.width(10.dp))
                    RadioButton(
                        selected = timeFormat == "24h",
                        onClick = { onTimeFormatChange("24h") }
                    )
                    AppText(text = stringResource(id = R.string._24h))
                }
            }

            // Date Format Section
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.date_format),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically // Aligns children vertically in the center
                ) {
                    RadioButton(
                        selected = dateFormat == "MM/DD",
                        onClick = { onDateFormatChange("MM/DD") }
                    )
                    AppText(text = stringResource(id = R.string.mm_dd))

                    Spacer(modifier = Modifier.width(10.dp))

                    RadioButton(
                        selected = dateFormat == "DD/MM",
                        onClick = { onDateFormatChange("DD/MM") }
                    )
                    AppText(text = stringResource(id = R.string.dd_mm))
                }
            }

            // Language Selection Section
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AppText(
                    text = stringResource(id = R.string.language),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                )

                LanguageDropdownMenu(modifier = Modifier
                    .weight(1.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdownMenu(modifier: Modifier) {
    val languages = listOf("English", "French", "German", "Italian", "Spanish", "Russian")
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(languages[0]) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedLanguage,
            onValueChange = {},
            readOnly = true,
            label = { AppText("Select Language") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor() // Anchors the dropdown to the text field
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { AppText(language) },
                    onClick = {
                        selectedLanguage = language
                        expanded = false
                    },

                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocale() {
    Locale(
        dateFormat = "MM:DD",
        timeFormat = "12h",
        selectedLanguage = "Spanish",
        onDateFormatChange = {},
        onTimeFormatChange = {},
        onLanguageChange = {})
}

