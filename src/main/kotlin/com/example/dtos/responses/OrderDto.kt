package com.example.dtos.responses

import kotlinx.serialization.Serializable
@Serializable
data class OrderDto(
    val id: String,
    val name: String,
)