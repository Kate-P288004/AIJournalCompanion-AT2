package com.kate.aijournalcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kate.aijournalcompanion.ui.theme.AIJournalCompanionTheme

data class JournalEntry(
    val text: String,
    val emotion: String,
    val advice: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIJournalCompanionTheme {
                AppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    var journalText by remember { mutableStateOf("") }
    var emotion by remember { mutableStateOf("Not analysed yet") }
    var advice by remember { mutableStateOf("Write an entry and press Analyze.") }

    // In-memory history (assessment allows in-memory storage)
    val history = remember { mutableStateListOf<JournalEntry>() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("AI Journal Companion") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Journal Entry", fontWeight = FontWeight.SemiBold)

            OutlinedTextField(
                value = journalText,
                onValueChange = { journalText = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                placeholder = { Text("Type how your day went...") }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        // Temporary local behaviour (we connect FastAPI next step)
                        val fakeEmotion = "JOY"
                        val fakeAdvice = "Do one small thing to celebrate today."
                        emotion = fakeEmotion
                        advice = fakeAdvice

                        if (journalText.isNotBlank()) {
                            history.add(
                                0,
                                JournalEntry(
                                    text = journalText.trim(),
                                    emotion = fakeEmotion,
                                    advice = fakeAdvice
                                )
                            )
                            journalText = ""
                        }
                    },
                    enabled = journalText.isNotBlank()
                ) {
                    Text("Analyze")
                }

                OutlinedButton(onClick = {
                    journalText = ""
                    emotion = "Not analysed yet"
                    advice = "Write an entry and press Analyze."
                }) {
                    Text("Clear")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Result", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text("Emotion: $emotion")
                    Text("Advice: $advice")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { /* next steps */ }, enabled = false) { Text("Sort") }
                OutlinedButton(onClick = { /* next steps */ }, enabled = false) { Text("Search") }
                OutlinedButton(onClick = { /* next steps */ }, enabled = false) { Text("Chart") }
                OutlinedButton(onClick = { /* next steps */ }, enabled = false) { Text("Help") }
            }

            Divider()

            Text("History", fontWeight = FontWeight.SemiBold)

            if (history.isEmpty()) {
                Text("No entries yet.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { entry ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(entry.emotion, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text(entry.text)
                                Spacer(Modifier.height(4.dp))
                                Text("Advice: ${entry.advice}")
                            }
                        }
                    }
                }
            }
        }
    }
}
