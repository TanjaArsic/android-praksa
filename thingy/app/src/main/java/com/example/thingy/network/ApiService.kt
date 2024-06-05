package com.example.thingy.network

import com.example.thingy.network.model.AxonDto
import com.example.thingy.network.model.FlowItemDto
import com.example.thingy.network.model.StreamDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("axons")
    suspend fun getAxons(): List<AxonDto>

    @GET("streams")
    suspend fun getStreams(): List<StreamDto>

    @GET("axons/{UiD}")
    suspend fun getAxonById(@Path("UiD") axonId: String): AxonDto

    @GET("streams/{UiD}")
    suspend fun getStreamById(@Path("UiD") streamId: String): StreamDto

    @GET("streams/{UiD}/flow")
    suspend fun getStreamFlow(@Path("UiD") streamId: String): List<FlowItemDto>

}