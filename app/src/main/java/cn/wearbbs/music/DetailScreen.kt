import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(navController: NavController, songId:String){
    var song by remember { mutableStateOf<SongDetail?>(null) }
    var songUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }


    LaunchedEffect(key1 = songId) {
        viewModelScope.launch {
            val response = MusicApiInstance.api.getSongDetail(songId)
            if(response.code == 200){
                song = response.songs?.firstOrNull()
            }
            val urlRes =  MusicApiInstance.api.getSongUrl(songId)
            if(urlRes.code == 200){
                songUrl = urlRes.data?.firstOrNull()?.url ?: ""
            }

            if(songUrl.isNotBlank()){
                val mediaItem = MediaItem.fromUri(songUrl)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
            }
        }
    }

    Column{
        if(song != null){
            Text(song?.name ?: "未知歌曲")
            Text("演唱者:${song?.ar?.joinToString(",") { it.name } ?: "未知演唱者"}")
            if(song?.al?.picUrl != null){
                Text("封面:${song?.al?.picUrl ?: "无封面"}")
            }
        }
        Button(onClick = {
            exoPlayer.release()
            navController.popBackStack()
        }) {
            Text("返回")
        }
    }
}
