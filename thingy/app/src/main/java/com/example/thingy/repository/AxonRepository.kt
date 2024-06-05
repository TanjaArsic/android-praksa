package com.example.thingy.repository

import com.example.thingy.domain.model.Axon
import com.example.thingy.network.ApiService
import javax.inject.Singleton
import com.example.thingy.domain.model.FlowItem
import com.example.thingy.domain.model.Stream
import com.example.thingy.network.model.toAxon
import com.example.thingy.network.model.toFlow
import com.example.thingy.network.model.toStream

@Singleton
class AxonRepository
//@Inject constructor
    (
    private val apiService: ApiService,

    ) : AxonRepositoryInterface {

    override suspend fun getAxonList(): Resource<List<Axon>> {
        return try {
            val axons = apiService.getAxons().map { it.toAxon() }
            println("Fetched axons: $axons")
            Resource.Success(axons)
        } catch (e: Exception) {
            println("Error fetching axons: ${e.message}")
            Resource.Error(e)
        }
    }

    override suspend fun getAxonById(axonId: String): Resource<Axon> {
        return try {
            val axon = apiService.getAxonById(axonId).toAxon()
            Resource.Success(axon)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getStreams(): Resource<List<Stream>> {
        return try {
            val streams = apiService.getStreams().map { it.toStream() }
            println("Fetched streams: $streams")
            Resource.Success(streams)
        } catch (e: Exception) {
            println("Error fetching streams: ${e.message}")
            Resource.Error(e)
        }
    }

    override suspend fun getStreamById(streamId: String): Resource<Stream> {
        return try {
            val stream = apiService.getStreamById(streamId).toStream()
            Resource.Success(stream)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getStreamFlow(streamId: String): Resource<List<FlowItem>> {
        return try {
            val flowItemList = apiService.getStreamFlow(streamId).map { it.toFlow() }
            println("Fetched flows: $flowItemList")
            Resource.Success(flowItemList)
        } catch (e: Exception) {
            println("Error fetching flows: ${e.message}")
            Resource.Error(e)
        }
    }

    override suspend fun getStreamsByAxonId(streamIds: List<String>): Resource<List<Stream>> {
        return try {
            val streams = mutableListOf<Stream>()

            streamIds.forEach { id ->
                when (val response = getStreamById(id)) {
                    is Resource.Success -> {
                        streams.add(response.data)
                    }

                    is Resource.Error -> {
                        println("Error fetching stream with id $id: ${response.exception.message}")
                        return Resource.Error(Exception("Failed to fetch all streams: Error at stream ID $id"))
                    }

                    else -> {}
                }
            }
            println("Fetched streams $streams with axon id")
            Resource.Success(streams)
        } catch (e: Exception) {
            println("Error fetching streams by id: ${e.message}")
            Resource.Error(e)
        }
    }

    override suspend fun fetchLatestStreamData(streamId: String): Resource<FlowItem> {
        return try {
            val flowItems = apiService.getStreamFlow(streamId)
            val latestFlowItem = flowItems.firstOrNull()
            if (latestFlowItem != null) {
                val value = latestFlowItem.value
                val timestamp = latestFlowItem.timestamp

                if (value != null || timestamp != null) {
                    val latestData = FlowItem(
                        value = value ?: "N/A",
                        timestamp = timestamp ?: "N/A"
                    )
                    Resource.Success(latestData)
                } else {
                    Resource.Error(Exception("Both value and timestamp are null"))
                }
            } else {
                Resource.Error(Exception("No data available"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}








