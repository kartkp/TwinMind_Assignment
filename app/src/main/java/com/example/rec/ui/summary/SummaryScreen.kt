package com.example.rec.ui.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.rec.model.Meeting
import com.example.rec.ui.theme.SurfaceHigh

@Composable
fun SummaryScreen(meetings: List<Meeting>) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(
				Brush.verticalGradient(
					listOf(
						MaterialTheme.colorScheme.background,
						MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
						MaterialTheme.colorScheme.background
					)
				)
			)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp)
	) {
		Text("Briefings", style = MaterialTheme.typography.headlineMedium)
		Text(
			"Stay on top of takeaways across your meetings.",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		meetings.takeIf { it.isNotEmpty() }?.forEach { meeting ->
			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
				shape = RoundedCornerShape(16.dp)
			) {
				Column(Modifier.padding(14.dp)) {
					Text("Meeting ${meeting.id.take(6)}", style = MaterialTheme.typography.titleMedium)
					Spacer(Modifier.height(6.dp))
					Text(meeting.summary.ifBlank { "No summary yet" })
				}
			}
		} ?: run {
			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(containerColor = SurfaceHigh),
				shape = RoundedCornerShape(16.dp)
			) {
				Column(Modifier.padding(14.dp)) {
					Text("No briefings yet", style = MaterialTheme.typography.titleMedium)
					Spacer(Modifier.height(6.dp))
					Text(
						"Summaries you generate will appear here for quick reference.",
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
		}
	}
}
