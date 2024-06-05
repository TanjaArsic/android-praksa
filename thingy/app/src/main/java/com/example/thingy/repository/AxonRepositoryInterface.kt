package com.example.thingy.repository

import com.example.thingy.domain.model.Axon
import com.example.thingy.domain.model.FlowItem
import com.example.thingy.domain.model.Stream

interface AxonRepositoryInterface {

    suspend fun getAxonList(): Resource<List<Axon>>
    suspend fun getAxonById(axonId: String): Resource<Axon>
    suspend fun getStreams(): Resource<List<Stream>>
    suspend fun getStreamById(streamId: String): Resource<Stream>
    suspend fun getStreamFlow(streamId: String): Resource<List<FlowItem>>
    suspend fun getStreamsByAxonId(streamIds: List<String>): Resource<List<Stream>>
    suspend fun fetchLatestStreamData(streamId: String): Resource<FlowItem>

}
