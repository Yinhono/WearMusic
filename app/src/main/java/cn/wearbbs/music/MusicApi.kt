package com.opensourcewear.wearmusic

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface MusicApi {
    /**
     * 搜索歌曲
     * @param keywords 搜索关键词
     */
    @GET("/search")
    suspend fun searchSongs(@Query("keywords") keywords: String): SearchResponse

    /**
     * 获取歌曲详情
     * @param ids 歌曲 ID，多个 ID 用逗号分隔
     */
    @GET("/song/detail")
    suspend fun getSongDetail(@Query("ids") ids: String): SongDetailResponse

    /**
     * 获取歌曲播放链接
     * @param id 歌曲 ID
     * @param level 音质等级，可选值为 "standard" 或 "hires"
     */
    @GET("/song/url/v1")
    suspend fun getSongUrl(@Query("id") id: String, @Query("level") level: String = "standard"): SongUrlResponse

    /**
     * 获取歌词
     * @param id 歌曲ID
     */
     @GET("/lyric")
    suspend fun getLyric(@Query("id") id: String): LyricResponse

     /**
      * 获取专辑详情
      * @param id 专辑ID
      */
    @GET("/album")
    suspend fun getAlbumDetail(@Query("id") id:String): AlbumDetailResponse
}

// 数据模型 (请根据你的 API 响应数据进行修改)
data class SearchResponse(
    val result: SearchResult?,
    val code: Int
)

data class SearchResult (
    val songs: List<Song>?,
    val songCount: Int
)

data class Song(
    val id: Long,
    val name: String,
    val artists: List<Artist>?
)

data class Artist (
    val name: String
)

data class SongDetailResponse(
    val songs: List<SongDetail>?,
    val code: Int
)

data class SongDetail (
    val al: Album?,
    val name: String?,
    val ar: List<Artist>?
)

data class Album(
    val picUrl: String?
)

data class SongUrlResponse(
    val data: List<SongUrlData>?,
    val code: Int
)

data class SongUrlData(
    val url: String?,
    val id: Long
)
data class LyricResponse(
    val lrc: LyricData?,
    val code: Int
)

data class LyricData(
    val lyric: String?
)

data class AlbumDetailResponse (
    val album: AlbumDetail,
    val code: Int
)
data class AlbumDetail(
  val name: String,
  val picUrl: String
)

// 创建 Retrofit 实例
object MusicApiInstance {
    val api: MusicApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://ncm.ling-yin.top/") // 请替换为你部署的域名
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }
}
