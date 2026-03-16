package com.example.music_play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.music_play.ui.theme.Music_PlayTheme

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: String
)

val favoriteSongs = listOf(
    Song(1, "Midnight City", "M83", "4:03"),
    Song(2, "Starboy", "The Weeknd", "3:50"),
    Song(3, "Levitating", "Dua Lipa", "3:23"),
    Song(4, "Blinding Lights", "The Weeknd", "3:20"),
    Song(5, "Circles", "Post Malone", "3:35"),
    Song(6, "Stay", "The Kid LAROI & Justin Bieber", "2:21"),
    Song(7, "Heat Waves", "Glass Animals", "3:58"),
    Song(8, "Save Your Tears", "The Weeknd", "3:35")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Music_PlayTheme {
                Music_PlayApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun Music_PlayApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var currentlyPlayingSong by rememberSaveable { mutableStateOf<Song?>(null) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentlyPlayingSong != null) {
                    NowPlayingBar(song = currentlyPlayingSong!!)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentDestination) {
                    AppDestinations.HOME -> HomeScreen(onPlaySong = { currentlyPlayingSong = it })
                    AppDestinations.FAVORITES -> FavoritesScreen(onPlaySong = { currentlyPlayingSong = it })
                    AppDestinations.PROFILE -> ProfileScreen()
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun HomeScreen(onPlaySong: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to Music Play", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Featured Playlist",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = { onPlaySong(favoriteSongs.random()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Shuffle Play Favorites")
        }
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Recently Added",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(favoriteSongs.take(3)) { song ->
                SongItem(song = song, onClick = { onPlaySong(song) })
            }
        }
    }
}

@Composable
fun FavoritesScreen(onPlaySong: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Favorites",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(favoriteSongs) { song ->
                SongItem(song = song, onClick = { onPlaySong(song) })
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = song.duration,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun NowPlayingBar(song: Song) {
    Column {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
            IconButton(onClick = { /* Previous */ }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
            }
            IconButton(onClick = { /* Pause/Play */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play/Pause")
            }
            IconButton(onClick = { /* Next */ }) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next")
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "User Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.size(24.dp))
        Icon(
            imageVector = Icons.Default.AccountBox,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = "Music Lover", style = MaterialTheme.typography.titleLarge)
        Text(text = "Premium Member", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("128", fontWeight = FontWeight.Bold)
                Text("Playlists", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("4.5k", fontWeight = FontWeight.Bold)
                Text("Followers", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("312", fontWeight = FontWeight.Bold)
                Text("Following", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    Music_PlayTheme {
        FavoritesScreen(onPlaySong = {})
    }
}
