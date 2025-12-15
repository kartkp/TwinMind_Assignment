package com.example.rec.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rec.model.Meeting
import com.example.rec.ui.theme.SurfaceHigh
import com.example.rec.ui.theme.SurfaceLow
import com.example.rec.ui.theme.TextMuted
import com.example.rec.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    meetings: List<Meeting>,
    onAddClick: () -> Unit,
    onMeetingClick: (Meeting) -> Unit
) {
    val totalMeetings = meetings.size
    val totalSeconds = meetings.sumOf { it.durationSeconds() }
    val lastMeeting = meetings.lastOrNull()

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Record")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(50.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {

                Text(
                    text = "TwinMind",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Meeting intelligence in one glance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        title = "Sessions",
                        value = "$totalMeetings",
                        highlight = "today",
                        icon = { Icon(Icons.Default.Mic, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Listening",
                        value = formatDuration(totalSeconds),
                        highlight = "total",
                        icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                FilledTonalButton(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Start a new capture")
                }

                Spacer(Modifier.height(18.dp))

                AnimatedVisibility(visible = lastMeeting != null) {
                    lastMeeting?.let { recent ->
                        HighlightCard(meeting = recent, onClick = { onMeetingClick(recent) })
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "All meetings",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (meetings.isEmpty()) {
                        item { EmptyStateCard(onAddClick) }
                    } else {
                        items(meetings) { meeting ->
                            MeetingCard(meeting, onMeetingClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MeetingCard(meeting: Meeting, onMeetingClick: (Meeting) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMeetingClick(meeting) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = "Meeting ${meeting.id.take(6)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Duration • ${meeting.duration}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (meeting.summary.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = meeting.summary.take(120) + if (meeting.summary.length > 120) "…" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    highlight: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                icon()
            }
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, color = TextMuted)
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(highlight, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun HighlightCard(meeting: Meeting, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(18.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Latest session", style = MaterialTheme.typography.labelLarge, color = TextMuted)
            Text("Meeting ${meeting.id.take(6)}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(6.dp))
            Text("Duration • ${meeting.duration}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (meeting.transcript.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(meeting.transcript.take(160) + if (meeting.transcript.length > 160) "…" else "")
            }
        }
    }
}

@Composable
private fun EmptyStateCard(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceLow),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("No meetings yet", style = MaterialTheme.typography.titleMedium)
            Text(
                "Tap the mic to start a recording and we will track your session here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FilledTonalButton(onClick = onAddClick) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Start recording")
            }
        }
    }
}

private fun Meeting.durationSeconds(): Int {
    val match = Regex("\\d+").find(duration)
    return match?.value?.toIntOrNull() ?: 0
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remaining = seconds % 60
    return if (minutes > 0) "${minutes}m ${remaining}s" else "${remaining}s"
}
