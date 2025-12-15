package com.example.rec.ui.meeting

import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rec.data.local.AppDatabase
import com.example.rec.data.local.entity.MeetingEntity
import com.example.rec.model.Meeting
import com.example.rec.network.ApiClient
import com.example.rec.network.SummaryRequest
import com.example.rec.ui.theme.SurfaceHigh
import com.example.twinmind.model.RecordingStatus
import com.example.twinmind.model.SummaryStatus
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun MeetingDetailScreen(meeting: Meeting) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    // 🎧 Audio
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // 📝 Transcription state
    var transcript by remember { mutableStateOf(meeting.transcript) }
    var isTranscribing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // 🧠 Summary state
    var title by remember { mutableStateOf<String?>(null) }
    var summary by remember { mutableStateOf<String?>(null) }
    var actionItems by remember { mutableStateOf<String?>(null) }
    var keyPoints by remember { mutableStateOf<String?>(null) }
    var isSummarizing by remember { mutableStateOf(false) }

    val db = remember(context) { AppDatabase.get(context) }

    LaunchedEffect(meeting.id) {
        scope.launch {
            db.meetingDao().insert(
                MeetingEntity(
                    meetingId = meeting.id,
                    audioPath = meeting.audioPath,
                    startTime = System.currentTimeMillis(),
                    recordingStatus = RecordingStatus.RECORDING,
                    transcript = transcript ?: "",
                    title = title,
                    summary = summary,
                    actionItems = actionItems,
                    keyPoints = keyPoints,
                    summaryStatus = SummaryStatus.COMPLETED
                )
            )
        }
    }





    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            Text(
                text = "Meeting detail",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Audio • ${meeting.duration}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                        }
                        Column {
                            Text("Playback", style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (isPlaying) "Audio is playing" else "Tap to listen",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isPlaying) {
                                mediaPlayer?.pause()
                                isPlaying = false
                            } else {
                                if (mediaPlayer == null) {
                                    mediaPlayer = MediaPlayer().apply {
                                        setDataSource(meeting.audioPath)
                                        prepare()
                                    }
                                }
                                mediaPlayer?.start()
                                isPlaying = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isPlaying) "Pause audio" else "Play audio")
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            ActionRow(
                onTranscribe = {
                    scope.launch {
                        try {
                            isTranscribing = true
                            error = null

                            val file = File(meeting.audioPath)
                            val requestBody = file.asRequestBody("audio/*".toMediaType())
                            val part = MultipartBody.Part.createFormData(
                                name = "audio",
                                filename = file.name,
                                body = requestBody
                            )

                            val response = ApiClient.transcriptionApi.transcribe(part)
                            val rawText = response.body()

                            transcript = try {
                                val json = org.json.JSONObject(rawText!!)
                                json.optString("transcript", rawText)
                            } catch (e: Exception) {
                                rawText ?: "Transcript Empty"
                            }

                        } catch (e: Exception) {
                            error = e.message ?: "Transcription failed"
                        } finally {
                            isTranscribing = false
                        }
                    }
                },
                onSummarize = {
                    scope.launch {
                        try {
                            isSummarizing = true
                            error = null

                            val response = ApiClient.summaryApi.analyze(
                                SummaryRequest(
                                    transcript = transcript ?: ""
                                )
                            )

                            val body = response.body()
                            if (body != null) {
                                title = body.title
                                summary = body.summary
                                actionItems = body.action_items.joinToString("\n• ", prefix = "• ")
                                keyPoints = body.key_points.joinToString("\n• ", prefix = "• ")
                            } else {
                                error = "Summary empty"
                            }

                        } catch (e: Exception) {
                            error = e.message ?: "Summary failed"
                        } finally {
                            isSummarizing = false
                        }
                    }
                },
                transcribeEnabled = !isTranscribing,
                summarizeEnabled = !isSummarizing && !transcript.isNullOrBlank(),
                isTranscribing = isTranscribing,
                isSummarizing = isSummarizing
            )

            Spacer(Modifier.height(12.dp))

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
            }

            TranscriptCard(
                transcript = transcript,
                onCopy = {
                    transcript?.let { clipboard.setText(AnnotatedString(it)) }
                },
                loading = isTranscribing
            )

            Spacer(Modifier.height(12.dp))

            SummaryCard(title, summary, actionItems, keyPoints)

            Spacer(Modifier.height(12.dp))

            FilledTonalButton(
                onClick = {
                    val shareText = """
                    ${title ?: "Meeting Summary"}

                    Summary:
                    ${summary ?: ""}

                    Action Items:
                    ${actionItems ?: ""}

                    Key Points:
                    ${keyPoints ?: ""}
                    """.trimIndent()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }

                    context.startActivity(
                        Intent.createChooser(intent, "Share Summary")
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !title.isNullOrBlank() || !summary.isNullOrBlank()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share summary")
            }
        }
    }
}

@Composable
private fun ActionRow(
    onTranscribe: () -> Unit,
    onSummarize: () -> Unit,
    transcribeEnabled: Boolean,
    summarizeEnabled: Boolean,
    isTranscribing: Boolean,
    isSummarizing: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilledTonalButton(
            modifier = Modifier.weight(1f),
            onClick = onTranscribe,
            enabled = transcribeEnabled
        ) {
            if (isTranscribing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(if (isTranscribing) "Transcribing" else "Transcribe")
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = onSummarize,
            enabled = summarizeEnabled
        ) {
            if (isSummarizing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(if (isSummarizing) "Summarizing" else "Generate summary")
        }
    }
}

@Composable
private fun TranscriptCard(transcript: String?, onCopy: () -> Unit, loading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Transcript", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onCopy, enabled = !transcript.isNullOrBlank()) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Copy")
                }
            }

            when {
                loading -> CircularProgressIndicator()
                transcript.isNullOrBlank() -> Text(
                    "No transcript yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> Text(
                    transcript,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String?,
    summary: String?,
    actionItems: String?,
    keyPoints: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Summary", style = MaterialTheme.typography.titleMedium)
            if (title.isNullOrBlank() && summary.isNullOrBlank()) {
                Text(
                    "Generate a summary to see highlights here.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                if (!title.isNullOrBlank()) {
                    Text(title, style = MaterialTheme.typography.headlineSmall)
                }

                if (!summary.isNullOrBlank()) {
                    Text(summary, style = MaterialTheme.typography.bodyLarge)
                }

                Divider()

                SummarySection("Action items", actionItems)
                SummarySection("Key points", keyPoints)
            }
        }
    }
}

@Composable
private fun SummarySection(title: String, value: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(value?.ifBlank { "-" } ?: "-", style = MaterialTheme.typography.bodyMedium)
    }
}
