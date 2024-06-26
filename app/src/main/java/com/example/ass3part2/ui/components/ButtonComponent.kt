package com.example.ass3part2.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/* Explanation
    This code defines a custom button component the UI buttons.
     It allows customization of text, click behavior, and layout.
     The preview function demonstrates the button's appearance with sample text and an onClick placeholder.
*/
@Composable
fun ButtonComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 8.dp)
    ) {
        Text(text = buttonText)
    }
}

@Preview
@Composable
fun ButtonComponentPreview(){
    ButtonComponent(onClick = { /*TODO*/ }, buttonText = "ADD TO LIST")
}