package com.example.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderDto(
    val name : String
)