package com.example.rec.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.rec.model.Meeting
import com.example.rec.ui.*
import com.example.rec.ui.meeting.MeetingDetailScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val meetings = remember { mutableStateListOf<Meeting>() }

    NavHost(navController, startDestination = "dashboard") {

        composable("dashboard") {
            DashboardScreen(
                meetings = meetings,
                onAddClick = { navController.navigate("record") },
                onMeetingClick = { meeting ->
                    navController.navigate("detail/${meeting.id}")
                }
            )
        }

        composable("record") {
            RecordingScreen(
                onSave = { meeting ->
                    meetings.add(meeting)
                    navController.popBackStack()
                }
            )
        }

        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val meeting = meetings.first {
                it.id == backStack.arguments!!.getString("id")
            }
            MeetingDetailScreen(meeting)
        }

    }
}
