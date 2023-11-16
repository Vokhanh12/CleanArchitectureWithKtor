package com.example.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderDto(
    val name : String
)
