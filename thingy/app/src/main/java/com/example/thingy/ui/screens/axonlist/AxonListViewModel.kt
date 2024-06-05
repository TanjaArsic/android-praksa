package com.example.thingy.ui.screens.axonlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thingy.domain.model.Axon
import com.example.thingy.domain.model.Stream
import com.example.thingy.repository.AxonRepositoryInterface
import com.example.thingy.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AxonListViewModel @Inject constructor(
    private val axonRepository: AxonRepositoryInterface
) : ViewModel() {


    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)
    val axon = mutableStateOf<Axon?>(null)
    val axonList = mutableStateOf(emptyList<Axon>())
    val streamListByAxon = mutableStateOf(emptyList<Stream>())
    val streamFlowData = mutableStateOf(emptyMap<String, Any>())

    val events = MutableSharedFlow<Events?>(replay = 0)

    init {
        getAxonList()
    }

    private fun getAxonList() {

        isLoading.value = true
        isError.value = null

        viewModelScope.launch {
            val result = axonRepository.getAxonList()
            isLoading.value = false
            when (result) {
                is Resource.Success -> axonList.value = result.data
                is Resource.Loading -> isLoading.value = true
                is Resource.Error -> isError.value = result.exception.message ?: "Unknown error"
            }
        }
    }

    fun fetchStreamsForAxon(axon: Axon?) {
        viewModelScope.launch {
            isLoading.value = true
            val result = axon?.let { axonRepository.getStreamsByAxonId(it.streams) }
            isLoading.value = false
            when (result) {
                is Resource.Success -> streamListByAxon.value = result.data
                is Resource.Error -> isError.value = result.exception.message ?: "Unknown error"
                else -> Unit
            }
        }
    }

    fun fetchLatestStreamData(streamId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = axonRepository.fetchLatestStreamData(streamId)
            isLoading.value = false
            when (result) {
                is Resource.Success -> {
                    streamFlowData.value =
                        streamFlowData.value.toMutableMap().apply { put(streamId, result.data) }
                    println("Fetched data for $streamId: ${result.data.value} at ${result.data.timestamp}")
                }

                is Resource.Error -> {
                    streamFlowData.value = streamFlowData.value.toMutableMap().apply {
                        result.exception.message?.let {

                            put(streamId, it)
                        }
                    }
                    println("Error fetching stream data: ${result.exception.message}")
                }

                else -> {}
            }
        }
    }

    fun onRetryClicked() {

            getAxonList()
    }

    fun onAxonSelected(axonId: String) {

        viewModelScope.launch {

            events.emit(Events.NavigateToAxonDetails(axonId))
        }
    }

    sealed class Events {
        data class NavigateToAxonDetails(
            val axonId: String
        ): Events()
    }

}