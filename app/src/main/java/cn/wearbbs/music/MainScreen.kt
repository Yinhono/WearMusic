import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    var searchKey by remember { mutableStateOf("") }
    var songs by remember { mutableStateOf<List<Song>?>(emptyList()) }


    Column {
        androidx.compose.material3.TextField(value = searchKey, onValueChange = { searchKey = it }, label = { Text("请输入关键词") })

        Button(onClick = {
            viewModel.viewModelScope.launch {
                val response = MusicApiInstance.api.searchSongs(searchKey)
                if (response.code == 200) {
                   songs = response.result?.songs
                }
            }
        }) {
            Text("搜索音乐")
        }

        LazyColumn {
            items(songs?: emptyList()) { song ->
                Text("${song.name} - ${song.artists?.joinToString(",") { it.name }}", modifier = Modifier.clickable {
                    navController.navigate("detail/${song.id}")
                })
            }
        }

    }
}
