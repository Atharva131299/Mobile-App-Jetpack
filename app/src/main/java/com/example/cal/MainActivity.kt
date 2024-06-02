package com.example.cal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cal.ui.theme.CalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply the custom theme to the app
            CalTheme {
                // Set the background color for the app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call the function to display the calculator screen
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    // Variables to keep track of the display text and numbers for calculations
    var displayText by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    // Layout for the calculator screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display area for the calculator
        Text(
            text = displayText,
            textAlign = TextAlign.End,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(2f) // Takes up twice the space as other rows in height
        )
        // Layout for the calculator buttons
        Column(modifier = Modifier.weight(8f)) {
            val buttons = listOf(
                listOf("C", "+/-", "%", "/"),
                listOf("7", "8", "9", "X"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "=")
            )
            // Create rows of buttons
            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    row.forEach { button ->
                        // Create individual buttons
                        CalculatorButton(
                            text = button,
                            modifier = Modifier
                                .weight(if (button == "0") 2f else 1f) // "0" button takes up twice the space in width
                                .aspectRatio(1f) // Square buttons
                                .padding(2.dp)
                        ) {
                            // Handle button click
                            onButtonClick(button, displayText, firstNumber, secondNumber, operation, { displayText = it }, { firstNumber = it }, { secondNumber = it }, { operation = it })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Button layout and styling
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(Color(0xFF6200EE))
            .clickable { onClick() } // Handle click events
    ) {
        Text(
            text = text,
            fontSize = 30.sp,
            color = Color.White
        )
    }
}

// Function to handle button clicks and update the calculator state
fun onButtonClick(
    buttonText: String,
    displayText: String,
    firstNumber: String,
    secondNumber: String,
    operation: String,
    setDisplayText: (String) -> Unit,
    setFirstNumber: (String) -> Unit,
    setSecondNumber: (String) -> Unit,
    setOperation: (String) -> Unit
) {
    when (buttonText) {
        "C" -> {
            // Clear all values
            setDisplayText("0")
            setFirstNumber("")
            setSecondNumber("")
            setOperation("")
        }
        "+/-" -> {
            // Toggle the sign of the current number
            setDisplayText((displayText.toDouble() * -1).toString())
        }
        "%" -> {
            // Convert the current number to a percentage
            setDisplayText((displayText.toDouble() / 100).toString())
        }
        "=", "+", "-", "X", "/" -> {
            if (firstNumber.isEmpty()) {
                // Set the first number and operation
                setFirstNumber(displayText)
                setOperation(buttonText)
                setDisplayText("0")
            } else if (secondNumber.isEmpty()) {
                // Set the second number and calculate the result
                setSecondNumber(displayText)
                val result = calculateResult(firstNumber, displayText, operation)
                setDisplayText(result)
                setFirstNumber(result)
                setSecondNumber("")
                setOperation(buttonText)
            }
        }
        else -> {
            // Update the display text with the clicked number or dot
            if (displayText == "0") {
                setDisplayText(buttonText)
            } else {
                setDisplayText(displayText + buttonText)
            }
        }
    }
}

// Function to calculate the result based on the operation
fun calculateResult(firstNumber: String, secondNumber: String, operation: String): String {
    val num1 = firstNumber.toDouble()
    val num2 = secondNumber.toDouble()
    return when (operation) {
        "+" -> (num1 + num2).toString()
        "-" -> (num1 - num2).toString()
        "X" -> (num1 * num2).toString()
        "/" -> (num1 / num2).toString()
        else -> "0"
    }
}
