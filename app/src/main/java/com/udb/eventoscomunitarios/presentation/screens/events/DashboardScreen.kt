package com.udb.eventoscomunitarios.presentation.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udb.eventoscomunitarios.presentation.theme.EventosComunitariosUDBTheme

data class EventItem(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val attendees: Int,
    val category: String,
    val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    // Sample data
    val sampleEvents = listOf(
        EventItem("1", "Festival Cultural UDB", "25 May - 2:00 PM", "Campus UDB", 45, "Cultural", "🎭"),
        EventItem("2", "Charla de IA", "28 May - 10:00 AM", "Aula Magna", 12, "Académico", "💻"),
        EventItem("3", "Torneo de Fútbol", "30 May - 8:00 AM", "Campo Deportivo", 8, "Deportes", "⚽")
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Eventos UDB",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* TODO: Drawer */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Notifications */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Próximos Eventos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(sampleEvents) { event ->
                EventCard(event = event)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { /* TODO: Create Event */ },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Crear Evento",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun EventCard(event: EventItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = event.emoji,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = event.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "📅 ${event.date}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "📍 ${event.location}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "👥 ${event.attendees} asistentes",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = event.category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Button(
                    onClick = { /* TODO: Register for event */ },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Asistir")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    EventosComunitariosUDBTheme {
        DashboardScreen()
    }
}