package com.example.rec.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.rec.model.Meeting
import com.example.rec.ui.*
import com.example.rec.ui.home.HomeScreen
import com.example.rec.ui.meeting.MeetingDetailScreen
import com.example.rec.ui.splash.SplashScreen
import com.example.rec.ui.summary.SummaryScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val meetings = remember { mutableStateListOf<Meeting>() }
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        NavHost(
            navController,
            startDestination = "home",
            enterTransition = { fadeIn(tween(400)) },
            exitTransition = { fadeOut(tween(300)) }
        ) {

            composable("home") {
                HomeScreen(onGetStarted = {
                    navController.navigate("main") {
                        popUpTo("home") { inclusive = true }
                    }
                })
            }

            composable("main") {
                MainScreenWithNav(
                    meetings = meetings,
                    onNavigateToRecord = { navController.navigate("record") },
                    onMeetingClick = { meeting ->
                        navController.navigate("detail/${meeting.id}")
                    }
                )
            }

            composable("record") {
                BackHandler {
                    navController.popBackStack()
                }
                RecordingScreen(
                    onSave = { meeting ->
                        meetings.add(meeting)
                        navController.popBackStack()
                    },
                    onCancel = {
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
}

@Composable
fun MainScreenWithNav(
    meetings: List<Meeting>,
    onNavigateToRecord: () -> Unit,
    onMeetingClick: (Meeting) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Record") },
                    label = { Text("Record") },
                    selected = selectedTab == 1,
                    onClick = { 
                        selectedTab = 1
                        onNavigateToRecord()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Description, contentDescription = "Summary") },
                    label = { Text("Summary") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> DashboardScreen(
                meetings = meetings,
                onAddClick = onNavigateToRecord,
                onMeetingClick = onMeetingClick
            )
            2 -> SummaryScreen(meetings = meetings)
        }
    }
}
