package org.avmedia.gShockSmartSyncCompose.ui.common

import AppText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    maxLength: Int,
    range: IntRange,
    fontSize: Int = 18,
) {
    Box(modifier = modifier.padding(start = 12.dp, end = 12.dp, top = 2.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Validate that the input is numeric before updating the state
                if (newValue.text.isEmpty() ||
                    (newValue.text.length <= maxLength && newValue.text.all { it.isDigit() or (it == '-') }
                            && (newValue.text.toIntOrNull() in range || newValue.text == "-"))) {
                    onValueChange(
                        newValue.copy(
                            selection = TextRange(newValue.text.length)
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().align(Alignment.Center), // Center align within Box
            placeholder = {
                AppText(text = placeholderText)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = VisualTransformation.None,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = fontSize.sp,
                textAlign = TextAlign.End
            )
        )
    }
}
