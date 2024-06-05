package com.example.thingy.network.model

import com.example.thingy.domain.model.FlowItem
fun FlowItemDto.toFlow(): FlowItem{
    return FlowItem(
        value = value,
        timestamp = timestamp
    )
}
