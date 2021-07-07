package dev.jdtech.jellyfin.repository

import dev.jdtech.jellyfin.api.JellyfinApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.model.api.*
import java.util.*

class JellyfinRepositoryImpl(private val jellyfinApi: JellyfinApi) : JellyfinRepository {
    override suspend fun getItem(itemId: UUID): BaseItemDto {
        val item: BaseItemDto
        withContext(Dispatchers.IO) {
            item = jellyfinApi.userLibraryApi.getItem(jellyfinApi.userId!!, itemId).content
        }
        return item
    }

    override suspend fun getItems(parentId: UUID?): List<BaseItemDto> {
        val items: List<BaseItemDto>
        withContext(Dispatchers.IO) {
            items = jellyfinApi.itemsApi.getItems(
                jellyfinApi.userId!!,
                parentId = parentId
            ).content.items ?: listOf()
        }
        return items
    }

    override suspend fun getSeasons(seriesId: UUID): List<BaseItemDto> {
        val seasons: List<BaseItemDto>
        withContext(Dispatchers.IO) {
            seasons = jellyfinApi.showsApi.getSeasons(seriesId, jellyfinApi.userId!!).content.items
                ?: listOf()
        }
        return seasons
    }

    override suspend fun getNextUp(seriesId: UUID): List<BaseItemDto> {
        val nextUpItems: List<BaseItemDto>
        withContext(Dispatchers.IO) {
            nextUpItems = jellyfinApi.showsApi.getNextUp(
                jellyfinApi.userId!!,
                seriesId = seriesId.toString()
            ).content.items ?: listOf()
        }
        return nextUpItems
    }

    override suspend fun getEpisodes(
        seriesId: UUID,
        seasonId: UUID,
        fields: List<ItemFields>?
    ): List<BaseItemDto> {
        val episodes: List<BaseItemDto>
        withContext(Dispatchers.IO) {
            episodes = jellyfinApi.showsApi.getEpisodes(
                seriesId, jellyfinApi.userId!!, seasonId = seasonId, fields = fields
            ).content.items ?: listOf()
        }
        return episodes
    }

    override suspend fun getStreamUrl(itemId: UUID): String {
        val streamUrl: String
        withContext(Dispatchers.IO) {
            /*val mediaInfo = jellyfinApi.mediaInfoApi.getPostedPlaybackInfo(
                itemId, PlaybackInfoDto(
                    userId = jellyfinApi.userId!!,
                    deviceProfile = DeviceProfile(
                        name = "Direct play all",
                        maxStaticBitrate = 1_000_000_000,
                        maxStreamingBitrate = 1_000_000_000,
                        codecProfiles = listOf(),
                        containerProfiles = listOf(),
                        directPlayProfiles = listOf(
                            DirectPlayProfile(
                                type = DlnaProfileType.VIDEO
                            ), DirectPlayProfile(type = DlnaProfileType.AUDIO)
                        ),
                        transcodingProfiles = listOf(),
                        responseProfiles = listOf(),
                        enableAlbumArtInDidl = false,
                        enableMsMediaReceiverRegistrar = false,
                        enableSingleAlbumArtLimit = false,
                        enableSingleSubtitleLimit = false,
                        ignoreTranscodeByteRangeRequests = false,
                        maxAlbumArtHeight = 1_000_000_000,
                        maxAlbumArtWidth = 1_000_000_000,
                        requiresPlainFolders = false,
                        requiresPlainVideoItems = false,
                        timelineOffsetSeconds = 0
                    ),
                    startTimeTicks = null,
                    audioStreamIndex = null,
                    subtitleStreamIndex = null,
                    maxStreamingBitrate = 1_000_000_000,
                )
            ).content*/
            streamUrl = jellyfinApi.videosApi.getVideoStreamUrl(
                itemId,
                static = true,
                mediaSourceId = itemId.toString()
            )
        }
        return streamUrl
    }
}