package com.example.dtos.extensions
import com.example.dtos.responses.OrderDto
import com.example.entities.Order

fun Order.toDto() : OrderDto {
    return OrderDto(this.id, this.name)
}