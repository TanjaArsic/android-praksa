package com.example.thingy.ui.screens.axondetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
class AxonDetailViewModel @Inject constructor(
    private val streamRepository: AxonRepositoryInterface,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val isError = mutableStateOf<String?>(null)
    val axon = mutableStateOf<Axon?>(null)
    val streamListByAxon = mutableStateOf(emptyList<Stream>())
    val streamFlowData = mutableStateOf(emptyMap<String, Any>())

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val axonId = savedStateHandle.get<String>("axonId")

    init {

        loadAxon()
    }

    fun fetchStreamsForAxon(axon: Axon?) {
        viewModelScope.launch {
            isLoading.value = true
            val result = axon?.let { streamRepository.getStreamsByAxonId(it.streams) }
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
            val result = streamRepository.fetchLatestStreamData(streamId)
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

    private fun loadAxon() {

        axonId?.let {

            isLoading.value = true
            isError.value = null

            viewModelScope.launch {

                val result = streamRepository.getAxonById(axonId)

                isLoading.value = false

                when (result) {
                    is Resource.Success -> axon.value = result.data
                    is Resource.Loading -> isLoading.value = true
                    is Resource.Error -> isError.value = result.exception.message ?: "Unknown error"
                }
            }
        }
    }

    fun onRetryClicked() {

        loadAxon()
    }


    fun navigateBack() {
        viewModelScope.launch {
            events.emit(Events.NavigateBack)
        }
    }

    sealed class Events {
        object NavigateBack : Events()
    }
}